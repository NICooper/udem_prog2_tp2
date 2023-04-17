package client.models;

import java.util.Arrays;

/**
 * Définition des sessions possibles pour toute l'application d'inscription.
 */
public class Sessions {
    private static final String[] sessionList = {"Automne", "Hiver", "Ete"};
    private static String regexString;

    /**
     * Retourne les noms de toutes les sessions.
     * @return Un array de String.
     */
    public static String[] getSessions() {
        return sessionList.clone();
    }

    /**
     * Retourne les noms des sessions en forme de String de Regex pattern.
     * Le String Regex fait que c'est facile de vérifier qu'un String contient le nom d'une session.
     * @return Un String.
     */
    public static String getRegexString() {
        if (regexString == null) {
            regexString = Arrays.stream(sessionList).reduce((a, b) -> a + "|" + b).get();
        }
        return regexString;
    }
}
