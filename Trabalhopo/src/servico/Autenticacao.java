package servico;

import modelo.Cliente;
import modelo.Funcionario;
import modelo.Utilizador;

import java.util.ArrayList;
import java.util.List;

public class Autenticacao {
    private List<Utilizador> utilizadores;

    public Autenticacao() {
        utilizadores = new ArrayList<>();

    }

    public boolean validarPassword(String password) {
        if (password == null || password.length() < 8) return false;
        int numeros = 0;
        boolean especial = false;
        String especiais = "!@#$%^&*()-_=+[]{};:,.<>?/\\|`~'\"";
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) numeros++;
            else if (especiais.indexOf(c) >= 0) especial = true;
        }
        return numeros >= 2 && especial;
    }

    /**
     * Valida número de telemóvel português:
     * - Exatamente 9 dígitos
     * - Começa por 9 (móvel) ou por +351 seguido de 9 dígitos começados por 9
     */
    public boolean validarTelemovel(String telemovel) {
        if (telemovel == null) return false;
        String t = telemovel.trim().replaceAll("\\s", "");
        // aceita +351 no início
        if (t.startsWith("+351")) t = t.substring(4);
        // deve ter 9 dígitos e começar por 9
        return t.matches("9[1236]\\d{7}");
    }

    public boolean existeUtilizador(String id) {
        for (Utilizador u : utilizadores)
            if (u.getIdIdentificacao().equalsIgnoreCase(id)) return true;
        return false;
    }

    private static final String CODIGO_FUNCIONARIO = "funcionarioupt";

    public Cliente registarCliente(String id, String password, String telemovel) {
        if (existeUtilizador(id)) return null;
        if (!validarPassword(password)) return null;
        if (!validarTelemovel(telemovel)) return null;
        Cliente c = new Cliente(id, password, telemovel);
        utilizadores.add(c);
        return c;
    }

    public Funcionario registarFuncionario(String id, String password, String nome, String codigoAcesso) {
        if (!CODIGO_FUNCIONARIO.equals(codigoAcesso)) return null;
        if (existeUtilizador(id)) return null;
        if (!validarPassword(password)) return null;
        Funcionario f = new Funcionario(id, password, nome);
        utilizadores.add(f);
        return f;
    }

    public Utilizador login(String id, String password) {
        for (Utilizador u : utilizadores) {
            if (u.getIdIdentificacao().equalsIgnoreCase(id) && u.getPassword().equals(password))
                return u;
        }
        return null;
    }
}