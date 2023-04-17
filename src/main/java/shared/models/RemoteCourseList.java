package shared.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Sous-classe de CourseList. Représente le cas où la List de Course est stockée sur un serveur et qu'il faut donc faire
 * un appel au serveur pour récupérer la List.
 */
public class RemoteCourseList extends CourseList {

    /**
     * Crée un nouveau RemoteCourseList.
     */
    public RemoteCourseList() {
        super();
    }

    /**
     * Fais un appel à un serveur pour récupérer une List de Course.
     * @return Un ModelResult qui contient la List de Course, l'état de la requête, et éventuellement un message d'erreur.
     */
    @Override
    public ModelResult<List<Course>> loadFilteredCourseList() {
        ModelResult<List<Course>> result = new ModelResult<>();

        if (this.getSessionFilter() == null) {
            result.success = false;
            result.message = "Une session valide n'a pas été sélectionnée comme filtre.";
            return result;
        }

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
        catch (Exception e) {
            result.success = false;
            result.message = "Une erreur est survenue. Veuillez contacter les administrateurs du serveur.";
        }

        return result;
    }
}
