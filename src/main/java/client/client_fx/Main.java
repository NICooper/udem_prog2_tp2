package client.client_fx;

import client.controllers.ClientController;
import client.models.RemoteCourseList;
import client.models.RemoteCourseRegistration;

public class Main {
    public static void main(String[] args) {
        ClientController controller = new ClientController(
                new RemoteCourseList(),
                new RemoteCourseRegistration()
        );

        ClientFx.main(controller);
    }
}
