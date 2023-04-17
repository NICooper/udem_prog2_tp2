package client.client_fx;

import client.controllers.ClientController;
import client.models.RemoteCourseList;
import client.models.RemoteCourseRegistration;

/**
 * Sert d'entrée pour le client GUI parce que JavaFx refuse de fonctionner si une classe Application est la fonction main.
 */
public class Main {
    private static String serverIpAddress = "127.0.0.1";
    int serverPort = 1337;

    /**
     * Démarre l'application d'inscription de cours de l'UdeM.
     * Initialise l'Application JavaFx, le controlleur pour client et les versions des modèles qui parlent au Server.
     * @param args pas utilisé.
     */
    public static void main(String[] args) {
        ClientController controller = new ClientController(
                new RemoteCourseList(),
                new RemoteCourseRegistration(se)
        );

        ClientFx.main(controller);
    }
}
