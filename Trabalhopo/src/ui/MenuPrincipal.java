package ui;

import modelo.Cliente;
import modelo.Funcionario;
import modelo.Utilizador;
import servico.Autenticacao;
import servico.GestorPedidos;
import servico.Menu;
import servico.Validacao;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class MenuPrincipal {
    private Scanner sc;
    private Autenticacao autenticacao;
    private Menu menu;
    private GestorPedidos gestorPedidos;

    public MenuPrincipal() {
        sc = new Scanner(System.in, StandardCharsets.UTF_8);
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
        System.out.println("\n--- Registar Cliente ---");
        String id = lerTextoObrigatorio("Nº de identificação: ");
        if (id == null) return;

        String password = lerPasswordValido();
        String telemovel = lerTelemovelValido();

        if (autenticacao.registarCliente(id, password, telemovel) != null) {
            System.out.println("Cliente registado com sucesso.");
        } else {
            System.out.println("Erro: ID já existe.");
        }
    }

    private void registarFuncionario() {
        System.out.println("\n--- Registar Funcionário ---");
        System.out.print("Código de acesso: ");
        String codigo = sc.nextLine().trim();

        String id = lerTextoObrigatorio("Nº de identificação: ");
        if (id == null) return;

        String nome = lerTextoObrigatorio("Nome: ");
        if (nome == null) return;

        String password = lerPasswordValido();

        Funcionario f = autenticacao.registarFuncionario(id, password, nome, codigo);
        if (f == null) {
            System.out.println("Registo falhado. Verifique o código de acesso ou se o ID já existe.");
        } else {
            System.out.println("Funcionário " + f.getNome() + " registado com sucesso.");
        }
    }

    private void fazerLogin() {
        System.out.println("\n--- Iniciar Sessão ---");
        String id = lerTextoObrigatorio("Nº de identificação: ");
        if (id == null) return;

        System.out.print("Password: ");
        String pw = sc.nextLine();

        Utilizador u = autenticacao.login(id, pw);
        if (u == null) {
            System.out.println("Credenciais inválidas.");
            return;
        }

        if (u instanceof Cliente) {
            Cliente cliente = (Cliente) u;
            new AreaCliente(sc, cliente, menu, gestorPedidos).abrir();
        } else if (u instanceof Funcionario) {
            Funcionario funcionario = (Funcionario) u;
            new AreaFuncionario(sc, funcionario, gestorPedidos, menu).abrir();
        } else {
            System.out.println("Tipo de utilizador desconhecido.");
        }
    }

    private String lerTextoObrigatorio(String prompt) {
        System.out.print(prompt);
        String valor = sc.nextLine().trim();
        if (valor.isEmpty()) {
            System.out.println("Valor obrigatório.");
            return null;
        }
        return valor;
    }

    private String lerPasswordValido() {
        while (true) {
            System.out.println("Palavra-passe (mín. 8 caracteres, 2 números, 1 caractere especial):");
            System.out.print("Password: ");
            String password = sc.nextLine();
            if (!Validacao.validarPassword(password)) {
                System.out.println("Password inválida. Requisitos: mínimo 8 caracteres, 2 números e 1 caractere especial.");
                continue;
            }
            return password;
        }
    }

    private String lerTelemovelValido() {
        while (true) {
            System.out.print("Telemóvel (ex: 912345678 ou +351912345678): ");
            String telemovel = sc.nextLine().trim();
            if (!Validacao.validarTelemovel(telemovel)) {
                System.out.println("Telemóvel inválido. Deve ser um número português válido.");
                continue;
            }
            return telemovel;
        }
    }

    public static void main(String[] args) {
        new MenuPrincipal().iniciar();
    }
}
