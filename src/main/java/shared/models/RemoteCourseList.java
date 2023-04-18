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
    private final String ipAddress;
    private final int port;
    private final String serverCommand;

    /**
     * Crée un nouveau RemoteCourseList.
     * @param ipAddress L'adresse IP du serveur.
     * @param port Le port du serveur à contacter.
     * @param serverCommand La commande en String qui correspond au commande du serveur pour le chargement de la liste de cours.
     */
    public RemoteCourseList(String ipAddress, int port, String serverCommand) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.serverCommand = serverCommand;
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

        try (Socket clientSocket = new Socket(this.ipAddress, this.port);
             ObjectOutputStream os = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream is = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            os.writeObject(this.serverCommand + " " + this.getSessionFilter());

            Object response = is.readObject();

            if (response instanceof ModelResult<?>) {
                ModelResult<List<Course>> modelresponse = (ModelResult<List<Course>>) response;
                if (((ModelResult<?>) response).success) {
                    this.courses = modelresponse.data;
                    result.data = this.courses;
                    result.success = true;
                }
                else {
                    result.success = false;
                    result.message = modelresponse.message;
                }
            }
            else if (response instanceof String) {
                result.success = false;
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
