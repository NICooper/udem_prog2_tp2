package server;

/**
 * Définit la forme que doit prendre les fonctions qui traitent les evenements du Server
 */
@FunctionalInterface
public interface EventHandler {
    void handle(String cmd, String arg);
}
