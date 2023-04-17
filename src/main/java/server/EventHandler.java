package server;

/**
 * Définit la forme que doit prendre les fonctions qui traitent les événements du Server
 */
@FunctionalInterface
public interface EventHandler {
    void handle(String cmd, String arg);
}
