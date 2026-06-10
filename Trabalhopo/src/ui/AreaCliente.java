package ui;

import modelo.*;
import servico.GestorPedidos;
import servico.Menu;

import java.util.List;
import java.util.Scanner;

public class AreaCliente {
    private Scanner sc;
    private Cliente cliente;
    private Menu menu;
    private GestorPedidos gestorPedidos;

    public AreaCliente(Scanner sc, Cliente cliente, Menu menu, GestorPedidos gp) {
        this.sc = sc;
        this.cliente = cliente;
        this.menu = menu;
        this.gestorPedidos = gp;
    }

    public void abrir() {
        while (true) {
            System.out.println("\n--- Área Cliente (" + cliente.getIdIdentificacao() + ") ---");
            System.out.println("1 - Consultar menu");
            System.out.println("2 - Realizar pedido");
            System.out.println("3 - Acompanhar estado do pedido");
            System.out.println("0 - Sair (logout)");
            System.out.print("Opção: ");
            String op = sc.nextLine().trim();
            switch (op) {
                case "1" -> consultarMenu();
                case "2" -> realizarPedido();
                case "3" -> acompanharPedido();
                case "0" -> { return; }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private void consultarMenu() {
        System.out.println("\n=== MENU ===");
        for (ItemMenu i : menu.getTodosOrdenados())
            System.out.println("- " + i.toStringCliente());
    }

    private void realizarPedido() {
        Pedido pedido = gestorPedidos.criarPedido(cliente);

        boolean continuar = true;
        while (continuar) {
            System.out.println("\n--- Realizar Pedido ---");
            System.out.println("Itens no carrinho: " + pedido.getItens().size() + "/10");
            System.out.println("1 - Adicionar bebida");
            System.out.println("2 - Adicionar salgado");
            System.out.println("3 - Adicionar doce");
            System.out.println("4 - Ver carrinho / remover item");
            System.out.println("5 - Confirmar pedido");
            System.out.println("0 - Cancelar");
            System.out.print("Opção: ");
            String op = sc.nextLine().trim();
            switch (op) {
                case "1" -> adicionarDoTipo(pedido, ItemTipo.BEBIDA);
                case "2" -> adicionarDoTipo(pedido, ItemTipo.SALGADO);
                case "3" -> adicionarDoTipo(pedido, ItemTipo.DOCE);
                case "4" -> verCarrinho(pedido);
                case "5" -> {
                    if (pedido.getItens().isEmpty()) {
                        System.out.println("Carrinho vazio.");
                    } else if (confirmar(pedido)) {
                        continuar = false;
                    }
                }
                case "0" -> {
                    gestorPedidos.cancelarPedido(pedido);
                    System.out.println("Pedido cancelado.");
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private void adicionarDoTipo(Pedido pedido, ItemTipo tipo) {
        if (pedido.getItens().size() >= 10) {
            System.out.println("Já atingiu o limite de 10 itens.");
            return;
        }
        List<ItemMenu> lista = menu.getPorTipo(tipo).stream()
                .filter(ItemMenu::temStock)
                .collect(java.util.stream.Collectors.toList());

        if (lista.isEmpty()) {
            System.out.println("Não existem itens disponíveis nesta categoria.");
            return;
        }

        System.out.println("\n-- " + tipo + " --");
        for (int i = 0; i < lista.size(); i++)
            System.out.println((i + 1) + " - " + lista.get(i).toStringCliente());
        System.out.print("Escolha (0 para voltar): ");
        try {
            int esc = Integer.parseInt(sc.nextLine().trim());
            if (esc == 0) return;
            if (esc < 1 || esc > lista.size()) {
                System.out.println("Inválido.");
                return;
            }

            ItemMenu escolhido = lista.get(esc - 1);
            int quantidade = pedirQuantidade(escolhido);
            if (quantidade <= 0) return;
            String alt = pedirAlteracao(escolhido);
            if (!gestorPedidos.adicionarItemAoPedido(pedido, escolhido, alt, quantidade)) {
                System.out.println("Não foi possível adicionar o item; limite de 10 itens atingido ou stock insuficiente.");
                return;
            }
            System.out.println("Adicionado " + quantidade + "x " + escolhido.getNome() + ".");
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
        }
    }

    private int pedirQuantidade(ItemMenu item) {
        System.out.print("Quantidade desejada (máximo " + item.getStock() + "): ");
        try {
            int qty = Integer.parseInt(sc.nextLine().trim());
            if (qty <= 0) {
                System.out.println("Quantidade inválida.");
                return 0;
            }
            if (qty > item.getStock()) {
                System.out.println("Stock insuficiente. Disponível: " + item.getStock());
                return 0;
            }
            return qty;
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
            return 0;
        }
    }

    private String pedirAlteracao(ItemMenu item) {
        List<String> opcoes = item.getAlteracoesPossiveis();

        System.out.println("\nAlterações disponíveis para " + item.getNome() + ":");
        System.out.println("  0 - Sem alteração (continuar)");
        for (int i = 0; i < opcoes.size(); i++)
            System.out.println("  " + (i + 1) + " - " + opcoes.get(i));
        System.out.println("  " + (opcoes.size() + 1) + " - Outra (escrever)");
        System.out.print("Opção (Enter ou 0 para nenhuma): ");

        String linha = sc.nextLine().trim();
        // Enter em branco = sem alteração
        if (linha.isEmpty()) return "nenhuma";

        try {
            int op = Integer.parseInt(linha);
            if (op == 0) return "nenhuma";
            if (op >= 1 && op <= opcoes.size()) return opcoes.get(op - 1);
            if (op == opcoes.size() + 1) {
                System.out.print("Indique a alteração: ");
                String livre = sc.nextLine().trim();
                return livre.isBlank() ? "nenhuma" : livre;
            }
        } catch (NumberFormatException ignored) { }
        return "nenhuma";
    }

    private void verCarrinho(Pedido pedido) {
        if (pedido.getItens().isEmpty()) { System.out.println("Carrinho vazio."); return; }
        System.out.println("\n-- Carrinho --");
        for (int i = 0; i < pedido.getItens().size(); i++)
            System.out.println((i + 1) + " - " + pedido.getItens().get(i));
        System.out.printf("Total: %.2f€%n", pedido.calcularTotal());
        System.out.print("Remover item? (nº ou 0 para voltar): ");
        try {
            int r = Integer.parseInt(sc.nextLine().trim());
            if (r > 0 && r <= pedido.getItens().size()) {
                if (gestorPedidos.removerItemDoPedido(pedido, r - 1)) {
                    System.out.println("Removido.");
                } else {
                    System.out.println("Não foi possível remover o item.");
                }
            }
        } catch (NumberFormatException e) { /* ignora */ }
    }

    private boolean confirmar(Pedido pedido) {
        System.out.println("\n--- Resumo ---");
        for (ItemPedido ip : pedido.getItens())
            System.out.println("- " + ip);
        System.out.printf("Total: %.2f€%n", pedido.calcularTotal());
        System.out.print("Confirmar pedido? (s/n): ");
        if (!sc.nextLine().trim().equalsIgnoreCase("s")) {
            System.out.println("Não confirmado.");
            return false;
        }

        System.out.println("Forma de pagamento:");
        System.out.println("1 - Dinheiro (pagar ao balcão)");
        System.out.println("2 - MBWay");
        System.out.print("Opção: ");
        String op = sc.nextLine().trim();
        if (op.equals("1")) {
            pedido.setFormaPagamento(FormaPagamento.DINHEIRO);
            pedido.setPago(false); // pagamento pendente — confirmar no balcão
            System.out.println("Dirija-se ao balcão para pagar antes de levantar o pedido.");
        } else if (op.equals("2")) {
            pedido.setFormaPagamento(FormaPagamento.MBWAY);
            pedido.setPago(true); // MBWay pago imediatamente
            System.out.print("Indique o nº MBWay: ");
            sc.nextLine();
            System.out.println("Pagamento MBWay efetuado.");
        } else {
            System.out.println("Forma inválida. Pedido cancelado.");
            gestorPedidos.cancelarPedido(pedido);
            return false;
        }

        gestorPedidos.registarPedido(pedido);
        System.out.println("Pedido enviado ao bar com estado PENDENTE.");
        return true;
    }

    private void acompanharPedido() {
        System.out.print("Nº da senha: ");
        try {
            int s = Integer.parseInt(sc.nextLine().trim());
            Pedido p = gestorPedidos.procurarPorSenha(s);
            if (p == null) { System.out.println("Senha não encontrada."); return; }
            if (!p.getCliente().getIdIdentificacao().equals(cliente.getIdIdentificacao())) {
                System.out.println("Esta senha não pertence à sua conta.");
                return;
            }
            System.out.println("Estado do pedido " + s + ": " + p.getEstado());
        } catch (NumberFormatException e) {
            System.out.println("Senha inválida.");
        }
    }
}
