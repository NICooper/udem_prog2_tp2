package views.client_fx;

import controllers.ClientController;
import shared.models.RemoteCourseList;
import shared.models.RemoteCourseRegistration;

public class Main {
    public static void main(String[] args) {
        ClientController controller = new ClientController(
                new RemoteCourseList(),
                new RemoteCourseRegistration()
        );

        ClientFx.main(controller);
    }
}
