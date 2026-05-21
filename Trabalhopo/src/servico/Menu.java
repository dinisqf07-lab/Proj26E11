package servico;

import modelo.ItemMenu;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Menu {

    private List<ItemMenu> itens;

    public Menu() {
        this.itens = new ArrayList<>();
    }

    public void adicionarItem(ItemMenu item) {
        itens.add(item);
    }

    public List<ItemMenu> getTodos() {
        return itens;
    }

    public ItemMenu procurarPorId(int id) {
        for (ItemMenu i : itens) {
            if (i.getId() == id) {
                return i;
            }
        }
        return null;
    }

    // Devolve todos os itens por ordem alfabética de nome
    public List<ItemMenu> listarTodosOrdenados() {
        List<ItemMenu> copia = new ArrayList<>(itens);
        copia.sort(Comparator.comparing(ItemMenu::getNome, String.CASE_INSENSITIVE_ORDER));
        return copia;
    }

    // Devolve só os itens de um certo tipo, por ordem alfabética
    public List<ItemMenu> listarPorTipo(String tipo) {
        List<ItemMenu> resultado = new ArrayList<>();
        for (ItemMenu i : itens) {
            if (i.getTipo().equalsIgnoreCase(tipo)) {
                resultado.add(i);
            }
        }
        resultado.sort(Comparator.comparing(ItemMenu::getNome, String.CASE_INSENSITIVE_ORDER));
        return resultado;
    }
}