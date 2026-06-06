package servico;

import modelo.Cliente;
import modelo.Utilizador;

import java.util.ArrayList;
import java.util.List;

public class Autenticacao {
    private List<Utilizador> utilizadores;

    public Autenticacao() {
        utilizadores = new ArrayList<>();
        utilizadores.add(new Funcionario ("F001", "Bar@2026", "João"));
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

    public existeCliente(String id) {
        for (Utilizador u : utilizadores)
            if (u.getIdIdentificacao().equalsIgnoreCase(id)) return true;
        return false;
    }

    public Cliente registarCliente(String id, String password, String telemovel) {
        if (existeCliente(id)) return null;
        if(!validarPassword(password)) return null;
        Cliente c = new Cliente(id, password, telemovel);
        utilizadores.add(c);
        return c;
    }

    public Utilizador login(String id, String password) {
        for (Utilizador u : utilizadores) {
            if (u.getIdIdentificacao().equalsIgnoreCase(id) && u.getPassword().equals(password))
                return u;
        }
        return null;
    }
}
