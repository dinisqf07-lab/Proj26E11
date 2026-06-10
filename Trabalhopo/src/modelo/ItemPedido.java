package modelo;

public class ItemPedido {
    private ItemMenu item;
    private String alteracao;
    private int quantidade;

    public ItemPedido(ItemMenu item, String alteracao, int quantidade) {
        this.item = item;
        this.alteracao = (alteracao == null || alteracao.isBlank()) ? "nenhuma" : alteracao;
        this.quantidade = Math.max(1, quantidade);
    }

    public ItemMenu getItem() { return item; }
    public String getAlteracao() { return alteracao; }
    public void setAlteracao(String alteracao) { this.alteracao = alteracao; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = Math.max(1, quantidade); }

    @Override
    public String toString() {
        return quantidade + "x " + item.getNome() + " [alteração: " + alteracao + "]";
    }
}