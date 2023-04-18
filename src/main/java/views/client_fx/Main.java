package views.client_fx;

import controllers.ClientController;
import shared.models.RemoteCourseList;
import shared.models.RemoteCourseRegistration;

/**
 * Sert d'entrée pour le client GUI parce que JavaFx refuse de fonctionner si une classe Application est la fonction main.
 */
public class Main {
    private static String serverIpAddress = "127.0.0.1";
    private static int serverPort = 1337;

    /**
     * Démarre l'application d'inscription de cours de l'UdeM.
     * Initialise l'Application JavaFx, le controlleur pour client et les versions des modèles qui parlent au Server.
     * @param args pas utilisé.
     */
    public static void main(String[] args) {
        ClientController controller = new ClientController(
                new RemoteCourseList(serverIpAddress, serverPort, "CHARGER"),
                new RemoteCourseRegistration(serverIpAddress, serverPort, "INSCRIRE")
        );

        ClientFx.main(controller);
    }
}
