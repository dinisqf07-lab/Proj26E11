package modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemMenu {
    private final String nome;
    private final double preco;
    private final String descricao;
    private final ItemTipo tipo;
    private final List<String> alteracoesPossiveis;
    private int stock;

    public ItemMenu(String nome, double preco, String descricao, ItemTipo tipo,
                    List<String> alteracoesPossiveis, int stock) {
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.tipo = tipo;
        this.alteracoesPossiveis = new ArrayList<>(alteracoesPossiveis);
        this.stock = Math.max(0, stock);
    }

    public String getNome() { return nome; }

    public double getPreco() { return preco; }

    public String getDescricao() { return descricao; }

    public ItemTipo getTipo() { return tipo; }

    public List<String> getAlteracoesPossiveis() {

        return Collections.unmodifiableList(alteracoesPossiveis);
    }

    public int getStock() { return stock; }

    public void setStock(int stock) { this.stock = Math.max(0, stock); }

    public void adicionarStock(int quantidade) {
        this.stock = Math.max(0, this.stock + quantidade);
    }

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
