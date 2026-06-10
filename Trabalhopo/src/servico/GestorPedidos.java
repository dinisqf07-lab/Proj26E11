package servico;

import modelo.*;

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

    public boolean adicionarItemAoPedido(Pedido pedido, ItemMenu item, String alteracao, int quantidade) {
        if (quantidade <= 0) return false;
        for (int i = 0; i < quantidade; i++) {
            if (!item.reduzirStock()) {
                item.adicionarStock(i);
                return false;
            }
        }
        if (!pedido.adicionarItem(new ItemPedido(item, alteracao, quantidade))) {
            item.adicionarStock(quantidade);
            return false;
        }
        return true;
    }

    public boolean removerItemDoPedido(Pedido pedido, int indice) {
        if (indice < 0 || indice >= pedido.getItens().size()) return false;
        ItemPedido itemPedido = pedido.getItens().get(indice);
        itemPedido.getItem().adicionarStock(itemPedido.getQuantidade());
        return pedido.removerItem(indice);
    }

    public void cancelarPedido(Pedido pedido) {
        for (ItemPedido ip : new ArrayList<>(pedido.getItens())) {
            ip.getItem().adicionarStock(ip.getQuantidade());
        }
        pedido.limparItens();
    }

    public void registarPedido(Pedido p) {
        pedidos.add(p);
        System.out.println("\n>>> [SMS para " + p.getCliente().getTelemovel() + "] A sua senha é: " + p.getSenha() + " <<<\n");
    }

    public Pedido procurarPorSenha(int senha) {
        for (Pedido p : pedidos)
            if (p.getSenha() == senha) return p;
        return null;
    }

    public List<Pedido> getPendentes() {
        return getPorEstado(PedidoEstado.PENDENTE);
    }

    public List<Pedido> getPorEstado(PedidoEstado estado) {
        return pedidos.stream()
                .filter(p -> p.getEstado() == estado)
                .collect(Collectors.toList());
    }

    public List<Pedido> getTodos() { return new ArrayList<>(pedidos); }

    public boolean alterarEstado(Pedido p, PedidoEstado novo) {
        PedidoEstado atual = p.getEstado();
        if (novo == PedidoEstado.EM_PREPARACAO && atual == PedidoEstado.PENDENTE) {
            p.setEstado(novo); return true;
        }
        if (novo == PedidoEstado.PRONTO && atual == PedidoEstado.EM_PREPARACAO) {
            p.setEstado(novo); return true;
        }
        if (novo == PedidoEstado.ENTREGUE && atual == PedidoEstado.PRONTO && p.isPago()) {
            p.setEstado(novo); return true;
        }
        return false;
    }

    public String gerarRelatorio() {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================\n");
        sb.append("       RELATÓRIO DE VENDAS DO DIA       \n");
        sb.append("========================================\n\n");

        List<Pedido> entregues = getPorEstado(PedidoEstado.ENTREGUE);
        int totalPedidos = pedidos.size();
        int totalEntregues = entregues.size();
        int totalPendentes = getPorEstado(PedidoEstado.PENDENTE).size();
        int totalEmPrep = getPorEstado(PedidoEstado.EM_PREPARACAO).size();
        int totalProntos = getPorEstado(PedidoEstado.PRONTO).size();

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

        java.util.Map<String, int[]> contagem = new java.util.LinkedHashMap<>();
        double totalReceita = 0;
        int pagDinheiro = 0, pagMBWay = 0;

        for (Pedido p : entregues) {
            totalReceita += p.calcularTotal();
            if (p.getFormaPagamento() == FormaPagamento.DINHEIRO) pagDinheiro++;
            else if (p.getFormaPagamento() == FormaPagamento.MBWAY) pagMBWay++;

            for (ItemPedido ip : p.getItens()) {
                String nome = ip.getItem().getNome();
                contagem.computeIfAbsent(nome, k -> new int[]{0});
                contagem.get(nome)[0] += ip.getQuantidade();
            }
        }

        sb.append("--- Itens vendidos ---\n");
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
