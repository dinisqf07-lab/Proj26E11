package servico;

import modelo.Cliente;
import modelo.Pedido;

import java.util.ArrayList;
import java.util.List;

public class GestorPedidos {
    private List<Pedido> pedidos;
    private int proximaSenha;

    public GestorPedidos() {
        pedidos = new ArrayList<>();
        proximaSenha = 1;
    }

    public Pedido criarPedido(Cliente cliente) {
        Pedido p = new Pedido(proximaSenha++, cliente);
        return p;
    }

    public void registarPedido(Pedido p) {
        pedidos.add(p);
        // simula envio SMS
        System.out.println("\n>>> [SMS para " + p.getCliente().getTelemovel() +
                "] A sua senha é: " + p.getSenha() + " <<<\n");
    }

    public Pedido procurarPorSenha(int senha) {
        for (Pedido p : pedidos)
            if (p.getSenha() == senha) return p;
        return null;
    }

    public List<Pedido> getPendentes() {
        List<Pedido> r = new ArrayList<>();
        for (Pedido p : pedidos)
            if (p.getEstado() == Pedido.Estado.PENDENTE) r.add(p);
        return r;
    }

    public List<Pedido> getTodos() { return pedidos; }

    public boolean alterarEstado(Pedido p, Pedido.Estado novo) {
        Pedido.Estado atual = p.getEstado();
        // transições permitidas:
        // PENDENTE -> EM_PREPARACAO
        // EM_PREPARACAO -> PRONTO
        // (regra do enunciado: só pode alterar se anterior estiver em "em preparação" ou "pronto")
        if (novo == Pedido.Estado.EM_PREPARACAO && atual == Pedido.Estado.PENDENTE) {
            p.setEstado(novo); return true;
        }
        if (novo == Pedido.Estado.PRONTO && atual == Pedido.Estado.EM_PREPARACAO) {
            p.setEstado(novo); return true;
        }
        return false;
    }
}