package ui;

import modelo.*;
import servico.GestorPedidos;
import servico.Menu;
import servico.Validacao;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AreaFuncionario {
    private Scanner sc;
    private Funcionario funcionario;
    private GestorPedidos gestorPedidos;
    private Menu menu;

    public AreaFuncionario(Scanner sc, Funcionario f, GestorPedidos gp, Menu menu) {
        this.sc = sc;
        this.funcionario = f;
        this.gestorPedidos = gp;
        this.menu = menu;
    }

    public void abrir() {
        while (true) {
            System.out.println("\n--- Área Funcionário (" + funcionario.getNome() + ") ---");
            System.out.println("1 - Ver pedidos pendentes");
            System.out.println("2 - Ver todos os pedidos (com filtros)");
            System.out.println("3 - Alterar estado de um pedido");
            System.out.println("4 - Gestão de pagamentos");
            System.out.println("5 - Dar baixa numa senha (marcar como ENTREGUE)");
            System.out.println("6 - Gerir stock do menu");
            System.out.println("7 - Exportar relatório do dia");
            System.out.println("0 - Sair (logout)");
            System.out.print("Opção: ");
            String op = sc.nextLine().trim();
            switch (op) {
                case "1" -> listarPendentes();
                case "2" -> listarComFiltro();
                case "3" -> alterarEstado();
                case "4" -> gestaoPagamentos();
                case "5" -> darBaixaSenha();
                case "6" -> gerirStock();
                case "7" -> exportarRelatorio();
                case "0" -> { return; }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    // --- 1. Pendentes ---

    private void listarPendentes() {
        List<Pedido> pendentes = gestorPedidos.getPendentes();
        if (pendentes.isEmpty()) { System.out.println("Não existem pedidos pendentes."); return; }
        System.out.println("\n=== Pedidos PENDENTES ===");
        for (Pedido p : pendentes) mostrarDetalhes(p);
    }

    // --- 2. Todos com filtro ---

    private void listarComFiltro() {
        System.out.println("\n=== Filtrar pedidos ===");
        System.out.println("1 - Todos");
        System.out.println("2 - Pendentes");
        System.out.println("3 - Em preparação");
        System.out.println("4 - Prontos");
        System.out.println("5 - Entregues");
        System.out.print("Filtro: ");
        String op = sc.nextLine().trim();

        List<Pedido> lista;
        String titulo;
        switch (op) {
            case "1" -> { lista = gestorPedidos.getTodos();                                 titulo = "TODOS"; }
            case "2" -> { lista = gestorPedidos.getPorEstado(PedidoEstado.PENDENTE);       titulo = "PENDENTES"; }
            case "3" -> { lista = gestorPedidos.getPorEstado(PedidoEstado.EM_PREPARACAO);  titulo = "EM PREPARAÇÃO"; }
            case "4" -> { lista = gestorPedidos.getPorEstado(PedidoEstado.PRONTO);         titulo = "PRONTOS"; }
            case "5" -> { lista = gestorPedidos.getPorEstado(PedidoEstado.ENTREGUE);       titulo = "ENTREGUES"; }
            default  -> { System.out.println("Filtro inválido."); return; }
        }

        if (lista.isEmpty()) { System.out.println("Nenhum pedido encontrado."); return; }
        System.out.println("\n=== Pedidos: " + titulo + " ===");
        for (Pedido p : lista) {
            String infoPag = p.getFormaPagamento() == null ? "—"
                    : p.getFormaPagamento() + (p.isPago() ? " [PAGO]" : " [NÃO PAGO]");
            System.out.println("Senha " + p.getSenha() +
                    " | Cliente: " + p.getCliente().getIdIdentificacao() +
                    " | Estado: " + p.getEstado() +
                    " | Pagamento: " + infoPag +
                    " | Total: " + String.format("%.2f€", p.calcularTotal()));
        }
    }

    // --- 3. Alterar estado (PENDENTE -> EM_PREPARACAO -> PRONTO) ---

    private void alterarEstado() {
        System.out.print("Nº da senha do pedido: ");
        try {
            int s = Integer.parseInt(sc.nextLine().trim());
            Pedido p = gestorPedidos.procurarPorSenha(s);
            if (p == null) { System.out.println("Pedido não encontrado."); return; }
            System.out.println("Estado atual: " + p.getEstado());
            System.out.println("Novo estado:");
            System.out.println("1 - EM_PREPARACAO");
            System.out.println("2 - PRONTO");
            System.out.print("Opção: ");
            String op = sc.nextLine().trim();
            PedidoEstado novo;
            if (op.equals("1"))      novo = PedidoEstado.EM_PREPARACAO;
            else if (op.equals("2")) novo = PedidoEstado.PRONTO;
            else { System.out.println("Inválido."); return; }

            if (gestorPedidos.alterarEstado(p, novo))
                System.out.println("Estado alterado para " + novo + ".");
            else
                System.out.println("Transição não permitida (estado atual: " + p.getEstado() + ").");
        } catch (NumberFormatException e) {
            System.out.println("Senha inválida.");
        }
    }

    // --- 4. Gestão de pagamentos ---

    private void gestaoPagamentos() {
        while (true) {
            System.out.println("\n=== Gestão de Pagamentos ===");
            System.out.println("1 - Ver pedidos por pagar (dinheiro)");
            System.out.println("2 - Marcar pedido como pago");
            System.out.println("3 - Consultar estado de pagamento de uma senha");
            System.out.println("0 - Voltar");
            System.out.print("Opção: ");
            String op = sc.nextLine().trim();
            switch (op) {
                case "1" -> listarPorPagar();
                case "2" -> marcarComoPago();
                case "3" -> consultarPagamento();
                case "0" -> { return; }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private void listarPorPagar() {
        List<Pedido> porPagar = gestorPedidos.getTodos().stream()
                .filter(p -> p.getFormaPagamento() == FormaPagamento.DINHEIRO && !p.isPago())
                .collect(Collectors.toList());

        if (porPagar.isEmpty()) {
            System.out.println("Não existem pedidos por pagar.");
            return;
        }
        System.out.println("\n--- Pedidos por pagar (dinheiro) ---");
        for (Pedido p : porPagar) {
            System.out.printf("Senha %d | Cliente: %s | Estado: %s | Total: %.2f€%n",
                    p.getSenha(), p.getCliente().getIdIdentificacao(),
                    p.getEstado(), p.calcularTotal());
        }
    }

    private void marcarComoPago() {
        System.out.print("Nº da senha: ");
        try {
            int s = Integer.parseInt(sc.nextLine().trim());
            Pedido p = gestorPedidos.procurarPorSenha(s);
            if (p == null) { System.out.println("Pedido não encontrado."); return; }

            if (p.isPago()) {
                System.out.println("Este pedido já está marcado como PAGO.");
                return;
            }
            if (p.getFormaPagamento() == FormaPagamento.MBWAY) {
                System.out.println("Pedido com pagamento MBWay — é marcado como pago automaticamente.");
                return;
            }

            System.out.printf("Senha %d | Cliente: %s | Total: %.2f€%n",
                    p.getSenha(), p.getCliente().getIdIdentificacao(), p.calcularTotal());
            System.out.print("Confirmar recebimento do pagamento em dinheiro? (s/n): ");
            if (!sc.nextLine().trim().equalsIgnoreCase("s")) {
                System.out.println("Operação cancelada.");
                return;
            }
            p.setPago(true);
            if (p.getEstado() == PedidoEstado.PRONTO && gestorPedidos.alterarEstado(p, PedidoEstado.ENTREGUE)) {
                System.out.println("Pedido " + s + " marcado como PAGO e ENTREGUE.");
            } else {
                System.out.println("Pedido " + s + " marcado como PAGO.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Senha inválida.");
        }
    }

    private void consultarPagamento() {
        System.out.print("Nº da senha: ");
        try {
            int s = Integer.parseInt(sc.nextLine().trim());
            Pedido p = gestorPedidos.procurarPorSenha(s);
            if (p == null) { System.out.println("Pedido não encontrado."); return; }

            String formaPag = p.getFormaPagamento() == null ? "não definido" : p.getFormaPagamento().toString();
            String estadoPag = p.isPago() ? "PAGO" : "NÃO PAGO";
            System.out.printf("%nSenha %d | Forma de pagamento: %s | Estado: %s | Total: %.2f€%n",
                    p.getSenha(), formaPag, estadoPag, p.calcularTotal());
        } catch (NumberFormatException e) {
            System.out.println("Senha inválida.");
        }
    }

    // --- 5. Dar baixa (PRONTO -> ENTREGUE, só se pago) ---

    private void darBaixaSenha() {
        System.out.print("Nº da senha a dar baixa: ");
        try {
            int s = Integer.parseInt(sc.nextLine().trim());
            Pedido p = gestorPedidos.procurarPorSenha(s);
            if (p == null) { System.out.println("Pedido não encontrado."); return; }

            if (p.getEstado() != PedidoEstado.PRONTO) {
                System.out.println("Só é possível dar baixa em pedidos com estado PRONTO.");
                System.out.println("Estado atual: " + p.getEstado());
                return;
            }

            if (!p.isPago()) {
                System.out.println("Não é possível marcar como ENTREGUE: pagamento ainda não confirmado.");
                System.out.println("Aceda à opção '4 - Gestão de pagamentos' para registar o pagamento.");
                return;
            }

            if (gestorPedidos.alterarEstado(p, PedidoEstado.ENTREGUE)) {
                System.out.println("Senha " + s + " marcada como ENTREGUE.");
                mostrarDetalhes(p);
            }
        } catch (NumberFormatException e) {
            System.out.println("Senha inválida.");
        }
    }

    // --- 6. Gerir stock ---

    private void gerirStock() {
        List<ItemMenu> itens = menu.getTodosParaGestao();
        System.out.println("\n=== Gestão de Stock ===");
        for (int i = 0; i < itens.size(); i++) {
            ItemMenu item = itens.get(i);
            System.out.printf("  %2d - %-20s | stock: %d%s%n",
                    (i + 1), item.getNome(), item.getStock(),
                    item.getStock() == 0 ? " [ESGOTADO]" : "");
        }
        System.out.print("Escolha o item a atualizar (0 para voltar): ");
        try {
            int esc = Integer.parseInt(sc.nextLine().trim());
            if (esc == 0) return;
            if (esc < 1 || esc > itens.size()) { System.out.println("Inválido."); return; }

            ItemMenu item = itens.get(esc - 1);
            System.out.println("Stock atual de \"" + item.getNome() + "\": " + item.getStock());
            System.out.print("Novo valor de stock: ");
            int novoStock = Integer.parseInt(sc.nextLine().trim());
            if (novoStock < 0) { System.out.println("Stock não pode ser negativo."); return; }
            item.setStock(novoStock);
            System.out.println("Stock atualizado para " + novoStock + ".");
        } catch (NumberFormatException e) {
            System.out.println("Valor inválido.");
        }
    }

    // --- 7. Exportar relatório ---

    private void exportarRelatorio() {
        String relatorio = gestorPedidos.gerarRelatorio();
        System.out.println(relatorio);

        System.out.print("Guardar relatório em ficheiro? (s/n): ");
        if (!sc.nextLine().trim().equalsIgnoreCase("s")) return;

        String nomeFicheiro = "relatorio_" + LocalDate.now() + ".txt";
        try (FileWriter fw = new FileWriter(nomeFicheiro)) {
            fw.write(relatorio);
            System.out.println("Relatório guardado em: " + nomeFicheiro);
        } catch (IOException e) {
            System.out.println("Erro ao guardar ficheiro: " + e.getMessage());
        }
    }

    // --- Utilitário de detalhe ---

    private void mostrarDetalhes(Pedido p) {
        String infoPagamento = p.getFormaPagamento() == null ? "não definido"
                : p.getFormaPagamento() + (p.isPago() ? " [PAGO]" : " [NÃO PAGO]");
        System.out.println("\nSenha: " + p.getSenha() +
                " | Cliente: " + p.getCliente().getIdIdentificacao() +
                " | Estado: " + p.getEstado() +
                " | Pagamento: " + infoPagamento);
        System.out.println("Itens:");
        for (ItemPedido ip : p.getItens()) {
            String aviso = "";
            String alt = Validacao.normalizarTexto(ip.getAlteracao()).toLowerCase();
            if (alt.contains("aquecer") || alt.contains("aquec")) aviso = "  <-- AQUECER";
            System.out.println("  - " + ip.getItem().getNome() +
                    " [alteração: " + ip.getAlteracao() + "]" + aviso);
        }
        System.out.printf("  Total: %.2f€%n", p.calcularTotal());
    }
}
