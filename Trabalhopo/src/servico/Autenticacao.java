package servico;

import modelo.Cliente;
import modelo.Funcionario;
import modelo.Utilizador;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Autenticacao {
    private List<Utilizador> utilizadores;
    private static final String CODIGO_FUNCIONARIO = "funcionarioupt";

    public Autenticacao() {
        utilizadores = new ArrayList<>();
        utilizadores.add(new Funcionario("F001", hashPassword("Bar@2024"), "João"));
    }

    public boolean existeUtilizador(String id) {
        for (Utilizador u : utilizadores)
            if (u.getIdIdentificacao().equalsIgnoreCase(id)) return true;
        return false;
    }

    public Cliente registarCliente(String id, String password, String telemovel) {
        if (existeUtilizador(id)) return null;
        if (!Validacao.validarPassword(password)) return null;
        if (!Validacao.validarTelemovel(telemovel)) return null;
        Cliente c = new Cliente(id, hashPassword(password), telemovel);
        utilizadores.add(c);
        return c;
    }

    public Funcionario registarFuncionario(String id, String password, String nome, String codigoAcesso) {
        if (!CODIGO_FUNCIONARIO.equals(codigoAcesso)) return null;
        if (existeUtilizador(id)) return null;
        if (!Validacao.validarPassword(password)) return null;
        Funcionario f = new Funcionario(id, hashPassword(password), nome);
        utilizadores.add(f);
        return f;
    }

    public Utilizador login(String id, String password) {
        String hashed = hashPassword(password);
        for (Utilizador u : utilizadores) {
            if (u.getIdIdentificacao().equalsIgnoreCase(id) && u.getPassword().equals(hashed))
                return u;
        }
        return null;
    }

    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 não está disponível.", e);
        }
    }
}
