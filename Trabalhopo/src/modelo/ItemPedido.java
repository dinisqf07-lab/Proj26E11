package modelo;

import java.util.SplittableRandom;

public class ItemPedido {
    private ItemMenu item;
    private String alteracao;

    public ItemPedido(ItemMenu item, String alteracao) {
        this.item = item;
        this.alteracao = (alteracao == null || alteracao.isBlank())?"Nenhuma" : alteracao;
    }

    public ItemMenu getItem () { return item; }
    public String getAlteracao() { return alteracao; }
    public void setAlteracao ( String alteracao) { this.alteracao = alteracao; }

    @Override
    public String toString() {
        return item.getNome() + "[Alteração: " + alteracao + "]";
    }
}
