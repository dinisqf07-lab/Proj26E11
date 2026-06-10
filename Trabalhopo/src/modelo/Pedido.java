package modelo;

import java.util.ArrayList;
import java.util.List;


public class Pedido {
    public enum Estado { PENDENTE, EM_PREPARACAO, PRONTO, ENTREGUE }
    public enum FormaPagamento {DINHEIRO, MBWAY }

    private int senha;
    private Cliente cliente;
    private List<ItemPedido> itens;
    private Estado estado;
    private FormaPagamento formaPagamento;
    private boolean pago;

    public Pedido (int senha, Cliente cliente) {
        this.senha = senha;
        this.cliente = cliente;
        this.itens = new ArrayList<>();
        this.estado = Estado.PENDENTE;
        this.pago = false;
    }

    public int getSenha() { return senha; }
    public Cliente getCliente() { return cliente; }
    public List<ItemPedido> getItens() { return itens; }
    public Estado getEstado() { return estado; }
    public void setEstado(Estado estado) { this.estado = estado; }
    public FormaPagamento getFormaPagamento() { return formaPagamento; }
    public void setFormaPagamento(FormaPagamento fp) { this.formaPagamento = fp; }
    public boolean isPago() { return pago; }
    public void setPago(boolean pago) { this.pago = pago; }

    public boolean adicionarItem(ItemPedido ip) {
        if (itens.size() >= 10) return false;
        itens.add(ip);
        return true;
    }

    public void removerItem(int indice) {
        if (indice >= 0 && indice < itens.size()) itens.remove(indice);
    }

    public double calcularTotal() {
        double total = 0;
        for (ItemPedido ip : itens) total += ip.getItem().getPreco();
        return total;
    }



}
