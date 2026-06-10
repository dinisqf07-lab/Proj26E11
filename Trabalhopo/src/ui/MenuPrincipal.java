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
            System.out.println("3 - Registar Funcionário");
            System.out.println("0 - Sair");
            System.out.print("Opção: ");
            String op = sc.nextLine().trim();
            switch (op) {
                case "1" -> fazerLogin();
                case "2" -> registarCliente();
                case "3" -> registarFuncionario();
                case "0" -> { System.out.println("Até breve!"); return; }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private void registarCliente() {
        System.out.print("Nº de identificação: ");
        String id = sc.nextLine().trim();
        if (id.isEmpty()) { System.out.println("ID obrigatório."); return; }

        System.out.println("Palavra-passe (mín. 8 caracteres, 2 números, 1 caractere especial):");
        System.out.print("Password: ");
        String pw = sc.nextLine();
        if (!autenticacao.validarPassword(pw)) {
            System.out.println("Password inválida. Requisitos: mínimo 8 caracteres, 2 números e 1 caractere especial.");
            return;
        }

        System.out.print("Telemóvel (ex: 912345678 ou +351912345678): ");
        String tel = sc.nextLine().trim();
        if (!autenticacao.validarTelemovel(tel)) {
            System.out.println("Telemóvel inválido. Deve ser um número português válido (ex: 912345678).");
            return;
        }

        if (autenticacao.registarCliente(id, pw, tel) != null)
            System.out.println("Cliente registado com sucesso.");
        else
            System.out.println("Erro: ID já existe.");
    }

    private void registarFuncionario() {
        System.out.print("Código de acesso: ");
        String codigo = sc.nextLine().trim();

        System.out.print("Nº de identificação: ");
        String id = sc.nextLine().trim();
        if (id.isEmpty()) { System.out.println("ID obrigatório."); return; }

        System.out.print("Nome: ");
        String nome = sc.nextLine().trim();
        if (nome.isEmpty()) { System.out.println("Nome obrigatório."); return; }

        System.out.println("Palavra-passe (mín. 8 caracteres, 2 números, 1 caractere especial):");
        System.out.print("Password: ");
        String pw = sc.nextLine();
        if (!autenticacao.validarPassword(pw)) {
            System.out.println("Password inválida. Requisitos: mínimo 8 caracteres, 2 números e 1 caractere especial.");
            return;
        }

        modelo.Funcionario f = autenticacao.registarFuncionario(id, pw, nome, codigo);
        if (f == null) {
            System.out.println("Registo falhado. Verifique o código de acesso ou se o ID já existe.");
        } else {
            System.out.println("Funcionário \"" + f.getNome() + "\" registado com sucesso.");
        }
    }

    private void fazerLogin() {
        System.out.print("Nº de identificação: ");
        String id = sc.nextLine().trim();
        System.out.print("Password: ");
        String pw = sc.nextLine();

        Utilizador u = autenticacao.login(id, pw);
        if (u == null) { System.out.println("Credenciais inválidas."); return; }

        if (u.getTipo().equals("CLIENTE")) {
            new AreaCliente(sc, (modelo.Cliente) u, menu, gestorPedidos).abrir();
        } else {
            new AreaFuncionario(sc, (modelo.Funcionario) u, gestorPedidos, menu).abrir();
        }
    }

    public static void main(String[] args) {
        new MenuPrincipal().iniciar();
    }
}