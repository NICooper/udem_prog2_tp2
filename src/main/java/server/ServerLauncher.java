package server;

/**
 * Le point de départ du serveur.
 */
public class ServerLauncher {
    /**
     *  Le port auquel le serveur écoute les clients
     */
    public final static int PORT = 1337;

    /**
     * Initialise le serveur avec le port spécifié et appelle le method run().
     * @param args pas utilisé
     */
    public static void main(String[] args) {
        Server server;
        try {
            server = new Server(PORT);
            System.out.println("Server is running...");
            server.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}