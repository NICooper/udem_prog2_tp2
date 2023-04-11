package shared.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RemoteCourseList extends CourseList {

    public RemoteCourseList() {
        super();
    }

    @Override
    public ModelResult<List<Course>> loadFilteredCourseList() {
        ModelResult<List<Course>> result = new ModelResult<>();
        try (Socket clientSocket = new Socket("127.0.0.1", 1337);
             ObjectOutputStream os = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream is = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            os.writeObject("CHARGER " + this.getSessionFilter());

            Object response = is.readObject();
            if (response instanceof ArrayList<?>) {
                this.courses = (List<Course>) response;
                result.data = this.courses;
                result.success = true;
            }
            else {
                result.success = false;
                result.message = "Erreur de serveur. Veuillez changer votre commande et réessayer.";
            }
        }
        catch (IOException e) {
            result.success = false;
            result.message = "Erreur de connexion au serveur. Veuillez réessayer plus tard.";
        }
        catch (ClassNotFoundException e) {
            result.success = false;
            result.message = "Erreur de serveur. Veuillez contacter les administrateurs du serveur.";
        }

        return result;
    }
}
