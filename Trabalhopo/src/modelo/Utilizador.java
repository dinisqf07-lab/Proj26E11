package modelo;

public abstract class Utilizador {
    protected String idIdentificacao;
    protected String password;

    public Utilizador(String idIdentificacao, String password) {
        this.idIdentificacao = idIdentificacao;
        this.password = password;
    }

    public String getIdIdentificacao() { return idIdentificacao; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public abstract TipoUtilizador getTipo();
}
