package modelo;

public class Funcionario extends Utilizador {
    private String nome;

    public Funcionario(String idIdentificacao, String password, String nome) {
        super(idIdentificacao, password);
        this.nome = nome;
    }

    public String getNome() { return nome; }

    @Override
    public String getTipo() { return "FUNCIONARIO"; }
}