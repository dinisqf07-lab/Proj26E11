package modelo;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
    private final int senha;
    private final Cliente cliente;
    private final List<ItemPedido> itens;
    private PedidoEstado estado;
    private FormaPagamento formaPagamento;
    private boolean pago;

    public Pedido(int senha, Cliente cliente) {
        this.senha = senha;
        this.cliente = cliente;
        this.itens = new ArrayList<>();
        this.estado = PedidoEstado.PENDENTE;
        this.pago = false;
    }

    public int getSenha() { return senha; }
    public Cliente getCliente() { return cliente; }
    public List<ItemPedido> getItens() {
        return java.util.Collections.unmodifiableList(itens);
    }
    public PedidoEstado getEstado() { return estado; }
    public void setEstado(PedidoEstado estado) { this.estado = estado; }
    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormaPagamento fp) { this.formaPagamento = fp; }
    public boolean isPago() { return pago; }
    public void setPago(boolean pago) { this.pago = pago; }

    /** MBWay fica automaticamente pago; dinheiro requer confirmação manual */
    public boolean precisaConfirmacaoPagamento() {
        return formaPagamento == FormaPagamento.DINHEIRO && !pago;
    }

    public boolean adicionarItem(ItemPedido ip) {
        if (itens.size() >= 10) return false;
        itens.add(ip);
        return true;
    }

    public boolean removerItem(int indice) {
        if (indice >= 0 && indice < itens.size()) {
            itens.remove(indice);
            return true;
        }
        return false;
    }

    public void limparItens() {
        itens.clear();
    }

    public double calcularTotal() {
        double total = 0;
        for (ItemPedido ip : itens) total += ip.getItem().getPreco();
        return total;
    }
}