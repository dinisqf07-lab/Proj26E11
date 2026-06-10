package servico;

import modelo.ItemMenu;
import modelo.ItemTipo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class Menu {
    private final List<ItemMenu> itens;

    public Menu() {
        itens = new ArrayList<>();
        carregarItensPadrao();
    }

    private void carregarItensPadrao() {
        itens.add(new ItemMenu("Água", 0.80, "", ItemTipo.BEBIDA,
                Arrays.asList("nenhuma", "fresca", "natural", "com gás"), 20));
        itens.add(new ItemMenu("Café", 0.70, "", ItemTipo.BEBIDA,
                Arrays.asList("nenhuma", "curto", "cheio", "meio cheio",
                        "descafeinado", "com leite", "sem açúcar"), 30));
        itens.add(new ItemMenu("Sumo de Laranja", 1.50, "Natural", ItemTipo.BEBIDA,
                Arrays.asList("nenhuma", "sem gelo", "com gelo", "sem açúcar"), 15));

        itens.add(new ItemMenu("Croissant Misto", 1.80,
                "Croissant com fiambre e queijo", ItemTipo.SALGADO,
                Arrays.asList("nenhuma", "aquecer", "sem queijo", "sem fiambre", "sem manteiga"), 10));
        itens.add(new ItemMenu("Lanche Misto", 2.00,
                "Pão com fiambre e queijo", ItemTipo.SALGADO,
                Arrays.asList("nenhuma", "aquecer", "sem queijo", "sem fiambre", "sem molho"), 10));
        itens.add(new ItemMenu("Tosta Mista", 2.20,
                "Pão de forma, fiambre e queijo", ItemTipo.SALGADO,
                Arrays.asList("nenhuma", "aquecer", "sem queijo", "sem fiambre", "sem manteiga"), 8));

        itens.add(new ItemMenu("Bolo de Arroz", 1.20, "", ItemTipo.DOCE,
                Arrays.asList("nenhuma", "aquecer", "para levar"), 12));
        itens.add(new ItemMenu("Donut", 1.50, "Coberto com chocolate", ItemTipo.DOCE,
                Arrays.asList("nenhuma", "sem cobertura", "para levar"), 10));
        itens.add(new ItemMenu("Pastel de Nata", 1.10, "", ItemTipo.DOCE,
                Arrays.asList("nenhuma", "aquecer", "com canela", "para levar"), 15));
    }

    public List<ItemMenu> getTodosOrdenados() {
        return itens.stream()
                .sorted(Comparator.comparing(ItemMenu::getNome))
                .collect(Collectors.toList());
    }

    public List<ItemMenu> getPorTipo(ItemTipo tipo) {
        return itens.stream()
                .filter(i -> i.getTipo() == tipo)
                .sorted(Comparator.comparing(ItemMenu::getNome))
                .collect(Collectors.toList());
    }

    public List<ItemMenu> getTodosParaGestao() {
        return java.util.Collections.unmodifiableList(itens);
    }
}
