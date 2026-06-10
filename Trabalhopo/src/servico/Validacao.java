package servico;

import java.text.Normalizer;

public class Validacao {
    private static final String ESPECIAIS = "!@#$%^&*()-_=+[]{};:,.<>?/\\|`~'\"";

    public static boolean validarPassword(String password) {
        if (password == null || password.length() < 8) return false;
        int numeros = 0;
        boolean especial = false;
        for (char c : password.toCharArray()) {
            if (Character.isDigit(c)) numeros++;
            else if (ESPECIAIS.indexOf(c) >= 0) especial = true;
        }
        return numeros >= 2 && especial;
    }

    public static boolean validarTelemovel(String telemovel) {
        if (telemovel == null) return false;
        String t = telemovel.trim().replaceAll("\\s+", "");
        if (t.startsWith("+351")) t = t.substring(4);
        return t.matches("9[1236]\\d{7}");
    }

    public static String normalizarTexto(String texto) {
        if (texto == null) return "";
        String normalizado = Normalizer.normalize(texto, Normalizer.Form.NFD);
        return normalizado.replaceAll("\\p{M}", "");
    }
}
