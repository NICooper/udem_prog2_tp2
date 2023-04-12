package server;

/**
 * Le point de connection qui démarre le serveur.
 */
public class ServerLauncher {
    /**
     *  Le port auquel le serveur doit se connecter
     */
    public final static int PORT = 1337;

    /**
     * Initialise le serveur avec le port spécifié et appelle le method run().
     * @param args pas utilisé
     * @throws Exception apparition d'une situation anormale qui conduirait à l'échec du programme
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