package Principal;

mport modelo.Funcionario;
import modelo.ItemPedido;
import modelo.Pedido;
import servico.GestorPedidos;

import java.util.List;
import java.util.Scanner;

public class AreaFuncionario {
    private Scanner sc;
    private Funcionario funcionario;
    private gestorPedidos gestorPedidos;

    public AreaFuncionario(Scanner sc, Funcionario f, GestorPedidos gp){
        this.sc = sc;
        this.funcionario = f;
        this.gestrPedidos = gp;
    }

    public void abrir() {
        while(true) {
            System.out.println("\n--- Área Funcionário (" + funcionario.getNome() + ") ---");
            System.out.println("1 - Ver pedidos pendentes");
            System.out.println("2 - Ver todos os pedidos");
            System.out.println("3 - Alterar estado de um pedido");
            System.out.println("0 - Sair (logout)");
            System.out.print("Opção: ");
            String op = sc.nextLine().trim();
            switch (op) {
                case "1" -> listarPendentes();
                case "2" -> listarTodos();
                case "3" -> alterarEstado();
                case "0" -> { return; }
                default -> System.out.println("Opção inválida.");

            }
        }
    }

    private void listarPendentes() {
        List<Pedido> pendentes = gestorpedidos.getPendentes();
        if (pendentes.isEmpty()) {
            System.out.println("Não existem pedidos pendentes.");
            return;
        }
        System.out.println("\n===Pedidos PEDENTES ===");
        for (Pedido p : pendentes) {
            mostrarDetalhes(p);
        }
    }

    private void listarTodos() {
        List<Pedido> todos = gestorPedidos.getTodos();
        if (todos.isEmpty()) {
            System.out.println("Não existem pedidos.");
            return;
        }
        System.out.println("\n=== Todos os pedidos ===");
        for (Pedido p : todos) {
            System.out.println("Senha " + p.getSenha() +
                    " | Cliente: " + p.getCliente().getIdIdentificacao() +
                    " | Estado: " + p.getEstado());
        }
    }

    private void mostrarDetalhes(Pedido p) {
        System.out.println("\nSenha: " + p.getSenha() +
                " | Cliente: " + p.getCliente().getIdIdentificacao() +
                " | Estado: " + p.getEstado() +
                " | Pagamento: " + p.getFormaPagamento());
        System.out.println("Itens:");
        for (ItemPedido ip : p.getItens()) {
            String aviso = "";
            String alt = ip.getAlteracao().toLowerCase();
            if (alt.contains("aquecer") || alt.contains("aquec"))
                aviso = "  <-- AQUECER";
            System.out.println("  - " + ip.getItem().getNome() +
                    " [alteração: " + ip.getAlteracao() + "]" + aviso);
        }
    }

    private void alterarEstado() {
        System.out.print("Nº da senha do pedido: ");
        try {
            int s = Integer.parseInt(sc.nextLine().trim());
            Pedido p = gestorPedidos.procurarPorSenha(s);
            if (p == null) {
                System.out.println("Pedido não encontrado.");
                return;
            }
            System.out.println("Estado atual: " + p.getEstado());
            System.out.println("Novo estado:");
            System.out.println("1 - EM_PREPARACAO");
            System.out.println("2 - PRONTO");
            System.out.print("Opção: ");
            String op = sc.nextLine().trim();
            Pedido.Estado novo;
            if (op.equals("1")) novo = Pedido.Estado.EM_PREPARACAO;
            else if (op.equals("2")) novo = Pedido.Estado.PRONTO;
            else { System.out.println("Inválido."); return; }

            if (gestorPedidos.alterarestado(p, novo))
                System.out.println("Estado alterado para " + novo);
            else
                System.out.println("Transição não permitida (atual: " + p.getEstado()+ ").");
        } catch (NumberFormatException e) {
            System.out.println("Senha inválida.");
        }
    }
}
