package modelo;

public class ItemMenu {

    private int id;
    private String nome;
    private double preco;
    private String descricao;
    private String tipo;   // "bebida", "salgado", "doce"

    public ItemMenu(int id, String nome, double preco, String descricao, String tipo) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.descricao = descricao;
        this.tipo = tipo;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getTipo() {
        return tipo;
    }

    @Override
    public String toString() {
        String linha = "[" + id + "] " + nome + " - " + String.format("%.2f", preco) + " €";
        if (descricao != null && !descricao.isEmpty()) {
            linha += " (" + descricao + ")";
        }
        return linha;
    }
}