package servico;

import modelo.Cliente;
import modelo.ItemPedido;
import modelo.Pedido;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestorPedidos {
    private List<Pedido> pedidos;
    private int proximaSenha;

    public GestorPedidos() {
        pedidos = new ArrayList<>();
        proximaSenha = 1;
    }

    public Pedido criarPedido(Cliente cliente) {
        return new Pedido(proximaSenha++, cliente);
    }

    public void registarPedido(Pedido p) {
        pedidos.add(p);
        // simula envio SMS
        System.out.println("\n>>> [SMS para " + p.getCliente().getTelemovel() +
                "] A sua senha é: " + p.getSenha() + " <<<\n");
    }

    public Pedido procurarPorSenha(int senha) {
        for (Pedido p : pedidos)
            if (p.getSenha() == senha) return p;
        return null;
    }

    public List<Pedido> getPendentes() {
        return getPorEstado(Pedido.Estado.PENDENTE);
    }

    public List<Pedido> getPorEstado(Pedido.Estado estado) {
        return pedidos.stream()
                .filter(p -> p.getEstado() == estado)
                .collect(Collectors.toList());
    }

    public List<Pedido> getTodos() {
        return pedidos;
    }

    public boolean alterarEstado(Pedido p, Pedido.Estado novo) {
        Pedido.Estado atual = p.getEstado();
        if (novo == Pedido.Estado.EM_PREPARACAO && atual == Pedido.Estado.PENDENTE) {
            p.setEstado(novo); return true;
        }
        if (novo == Pedido.Estado.PRONTO && atual == Pedido.Estado.EM_PREPARACAO) {
            p.setEstado(novo); return true;
        }
        if (novo == Pedido.Estado.ENTREGUE && atual == Pedido.Estado.PRONTO && p.isPago()) {
            p.setEstado(novo); return true;
        }
        return false;
    }

    // Relatório diário

    public String gerarRelatorio() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("       RELATÓRIO DE VENDAS DO DIA       \n");
        sb.append("========================================\n\n");

        List<Pedido> entregues = getPorEstado(Pedido.Estado.ENTREGUE);
        int totalPedidos = pedidos.size();
        int totalEntregues = entregues.size();
        int totalPendentes = getPorEstado(Pedido.Estado.PENDENTE).size();
        int totalEmPrep = getPorEstado(Pedido.Estado.EM_PREPARACAO).size();
        int totalProntos = getPorEstado(Pedido.Estado.PRONTO).size();

        sb.append(String.format("Total de pedidos registados : %d%n", totalPedidos));
        sb.append(String.format("  - Entregues               : %d%n", totalEntregues));
        sb.append(String.format("  - Prontos (por entregar)  : %d%n", totalProntos));
        sb.append(String.format("  - Em preparação           : %d%n", totalEmPrep));
        sb.append(String.format("  - Pendentes               : %d%n", totalPendentes));
        sb.append("\n");

        if (entregues.isEmpty()) {
            sb.append("Não existem pedidos entregues para reportar.\n");
            return sb.toString();
        }

        // Contagem e receita por item
        java.util.Map<String, int[]> contagem = new java.util.LinkedHashMap<>();
        // int[0] = quantidade, int[1] = receita*100 (para evitar double)
        double totalReceita = 0;
        int pagDinheiro = 0, pagMBWay = 0;

        for (Pedido p : entregues) {
            totalReceita += p.calcularTotal();
            if (p.getFormaPagamento() == Pedido.FormaPagamento.DINHEIRO) pagDinheiro++;
            else if (p.getFormaPagamento() == Pedido.FormaPagamento.MBWAY) pagMBWay++;

            for (ItemPedido ip : p.getItens()) {
                String nome = ip.getItem().getNome();
                contagem.computeIfAbsent(nome, k -> new int[]{0});
                contagem.get(nome)[0]++;
            }
        }

        sb.append("--- Itens vendidos ---\n");
        // ordenar por quantidade descendente
        contagem.entrySet().stream()
                .sorted((a, b) -> b.getValue()[0] - a.getValue()[0])
                .forEach(e -> sb.append(String.format("  %-20s : %d unidade(s)%n",
                        e.getKey(), e.getValue()[0])));

        sb.append("\n--- Pagamentos ---\n");
        sb.append(String.format("  Dinheiro : %d pedido(s)%n", pagDinheiro));
        sb.append(String.format("  MBWay    : %d pedido(s)%n", pagMBWay));

        sb.append(String.format("%n--- RECEITA TOTAL (entregues): %.2f€ ---%n", totalReceita));
        sb.append("========================================\n");
        return sb.toString();
    }
}