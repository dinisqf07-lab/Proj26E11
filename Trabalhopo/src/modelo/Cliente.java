package modelo;

public class Cliente extends Utilizador {
    private String telemovel;

    public Cliente(String idIdentificacao, String password, String telemovel) {
        super(idIdentificacao, password);
        this.telemovel = telemovel;
    }

    public String getTelemovel() { return telemovel; }
    public void setTelemovel(String telemovel) { this.telemovel = telemovel; }

    @Override
    public String getTipo() { return "CLIENTE"; }
}