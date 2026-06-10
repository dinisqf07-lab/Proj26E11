package ui;

import modelo.Utilizador;
import servico.Autenticacao;
import servico.GestorPedidos;
import servico.Menu;

import java.util.Scanner;

public class MenuPrincipal {
    private Scanner sc;
    private Autenticacao autenticacao;
    private Menu menu;
    private GestorPedidos gestorPedidos;

    public MenuPrincipal() {
        sc = new Scanner(System.in);
        autenticacao = new Autenticacao();
        menu = new Menu();
        gestorPedidos = new GestorPedidos();
    }

    public void iniciar() {
        while (true) {
            System.out.println("\n====== BAR UPT ======");
            System.out.println("1 - Login");
            System.out.println("2 - Registar Cliente");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");
            String op = sc.nextLine().trim();
            switch (op) {
                case "1" -> fazerLogin();
                case "2" -> registar();
                case "0" -> { System.out.println("Até breve!"); return; }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private void registar() {
        System.out.print("Nº de identificação: ");
        String id = sc.nextLine().trim();
        if (id.isEmpty()) { System.out.println("ID obrigatório."); return; }

        System.out.println("Palavra-passe (mín. 8 caracteres, 2 números, 1 caractere especial):");
        System.out.print("Password: ");
        String pw = sc.nextLine();
        if (!autenticacao.validarPassword(pw)) {
            System.out.println("Password inválida.");
            return;
        }
        System.out.print("Telemóvel: ");
        String tel = sc.nextLine().trim();

        if (autenticacao.registarCliente(id, pw, tel) != null)
            System.out.println("Cliente registado com sucesso.");
        else
            System.out.println("Erro: ID já existe.");
    }

    private void fazerLogin() {
        System.out.print("Nº de identificação: ");
        String id = sc.nextLine().trim();
        System.out.print("Password: ");
        String pw = sc.nextLine();

        Utilizador u = autenticacao.login(id, pw);
        if (u == null) {
            System.out.println("Credenciais inválidas.");
            return;
        }

        if (u.getTipo().equals("CLIENTE")) {
            new AreaCliente(sc, (modelo.Cliente) u, menu, gestorPedidos).abrir();
        } else {
            new AreaFuncionario(sc,  u, gestorPedidos).abrir();
        }
    }

    public static void main(String[] args) {
        new MenuPrincipal().iniciar();
    }
}