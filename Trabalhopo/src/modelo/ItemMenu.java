package modelo;

import java.util.List;

public class ItemMenu {
    public enum Tipo { BEBIDA, SALGADO, DOCE }

    private final String nome;
    private final double preco;
    private final String descricao;
    private final Tipo tipo;
    private final List<String> alteracoesPossiveis;
    private int stock;

    public ItemMenu(String nome, double preco, String descricao, Tipo tipo,
                    List<String> alteracoesPossiveis, int stock) {
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.tipo = tipo;
        this.alteracoesPossiveis = alteracoesPossiveis;
        this.stock = stock;
    }

    public String getNome() { return nome; }
    public double getPreco() { return preco; }
    public String getDescricao() { return descricao; }
    public Tipo getTipo() { return tipo; }
    public List<String> getAlteracoesPossiveis() { return alteracoesPossiveis; }
    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = Math.max(0, stock); }

    public boolean temStock() { return stock > 0; }

    public boolean reduzirStock() {
        if (stock <= 0) return false;
        stock--;
        return true;
    }

    /** Usado na área do cliente — sem informação de stock */
    public String toStringCliente() {
        String desc = (descricao == null || descricao.isBlank()) ? "" : " - " + descricao;
        return String.format("%s (%.2f€)%s", nome, preco, desc);
    }

    /** Usado na área do funcionário — mostra stock e estado */
    @Override
    public String toString() {
        String desc = (descricao == null || descricao.isBlank()) ? "" : " - " + descricao;
        String stockInfo = stock == 0 ? " [ESGOTADO]" : " [stock: " + stock + "]";
        return String.format("%s (%.2f€)%s%s", nome, preco, desc, stockInfo);
    }
}
