package servico;

import modelo.ItemMenu;

import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;


public class Menu {
    private List<ItemMenu> itens;

    public Menu() {
        itens = new ArrayList<>();
        carregarItensPadrao();
    }

    private void carregarItensPadrao() {
        // Bebidas
        itens.add(new ItemMenu("Água", 0.80, "", ItemMenu.Tipo.BEBIDA,
                List.of("nenhuma", "fresca", "natural", "com gás"), 20));
        itens.add(new ItemMenu("Café", 0.70, "", ItemMenu.Tipo.BEBIDA,
                List.of("nenhuma", "curto", "cheio", "meio cheio",
                        "descafeinado", "com leite", "sem açúcar"), 30));
        itens.add(new ItemMenu("Sumo de Laranja", 1.50, "Natural", ItemMenu.Tipo.BEBIDA,
                List.of("nenhuma", "sem gelo", "com gelo", "sem açúcar"), 15));

        // Salgados
        itens.add(new ItemMenu("Croissant Misto", 1.80,
                "Croissant com fiambre e queijo", ItemMenu.Tipo.SALGADO,
                List.of("nenhuma", "aquecer", "sem queijo", "sem fiambre", "sem manteiga"), 10));
        itens.add(new ItemMenu("Lanche Misto", 2.00,
                "Pão com fiambre e queijo", ItemMenu.Tipo.SALGADO,
                List.of("nenhuma", "aquecer", "sem queijo", "sem fiambre", "sem molho"), 10));
        itens.add(new ItemMenu("Tosta Mista", 2.20,
                "Pão de forma, fiambre e queijo", ItemMenu.Tipo.SALGADO,
                List.of("nenhuma", "aquecer", "sem queijo", "sem fiambre", "sem manteiga"), 8));

        // Doces
        itens.add(new ItemMenu("Bolo de Arroz", 1.20, "", ItemMenu.Tipo.DOCE,
                List.of("nenhuma", "aquecer", "para levar"), 12));
        itens.add(new ItemMenu("Donut", 1.50, "Coberto com chocolate", ItemMenu.Tipo.DOCE,
                List.of("nenhuma", "sem cobertura", "para levar"), 10));
        itens.add(new ItemMenu("Pastel de Nata", 1.10, "", ItemMenu.Tipo.DOCE,
                List.of("nenhuma", "aquecer", "com canela", "para levar"), 15));
    }

    public List<ItemMenu> getTodosOrdenados() {

        Collator collator = Collator.getInstance(new Locale("pt", "PT"));

        return itens.stream()
                .sorted((a, b) -> collator.compare(a.getNome(), b.getNome()))
                .collect(Collectors.toList());
    }

    public List<ItemMenu> getPorTipo(ItemMenu.Tipo tipo) {

        Collator collator = Collator.getInstance(new Locale("pt", "PT"));

        return itens.stream()
                .filter(i -> i.getTipo() == tipo)
                .sorted((a,b) -> collator.compare(a.getNome(), b.getNome()))
                .collect(Collectors.toList());
    }

    /** Devolve todos os itens (com stock e sem stock) para gestão pelo funcionário */
    public List<ItemMenu> getTodosParaGestao() {
        return new ArrayList<>(itens);
    }
}