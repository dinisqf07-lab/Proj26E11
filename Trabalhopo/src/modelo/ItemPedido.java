package modelo;

public class ItemPedido {
    private ItemMenu item;
    private String alteracao; // "sem queijo", "aquecer", "nenhuma", etc.

    public ItemPedido(ItemMenu item, String alteracao) {
        this.item = item;
        this.alteracao = (alteracao == null || alteracao.isBlank()) ? "nenhuma" : alteracao;
    }

    public ItemMenu getItem() { return item; }
    public String getAlteracao() { return alteracao; }
    public void setAlteracao(String alteracao) { this.alteracao = alteracao; }

    @Override
    public String toString() {
        return item.getNome() + " [alteração: " + alteracao + "]";
    }
}