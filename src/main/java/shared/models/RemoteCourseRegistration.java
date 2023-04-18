package shared.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * RemoteCourseRegistration sert à sauvegarder un RegistrationForm sur un serveur.
 */
public class RemoteCourseRegistration extends CourseRegistration {
    private final String ipAddress;
    private final int port;
    private final String serverCommand;

    /**
     * Crée un nouveau RemoteCourseRegistration.
     * @param ipAddress L'adresse IP du serveur.
     * @param port Le port du serveur à contacter.
     * @param serverCommand La commande en String qui correspond au commande du serveur pour la sauvegarde d'inscription.
     */
    public RemoteCourseRegistration(String ipAddress, int port, String serverCommand) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.serverCommand = serverCommand;
    }

    /**
     * Fait une demande au serveur spécifié de sauvgarder le formulaire d'inscription qui a été donné à CourseRegistration.
     * @return Un ModelResult qui contient le formulaire sauvegardé et des informations sur le succès de l'opération de
     * sauvegarde.
     */
    @Override
    public ModelResult<RegistrationForm> register() {
        ModelResult<RegistrationForm> result = new ModelResult<>();

        try (Socket clientSocket = new Socket(this.ipAddress, this.port);
             ObjectOutputStream os = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream is = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            os.writeObject(this.serverCommand);

            os.writeObject(this.validatedForm.form);


            Object obj = is.readObject();
            if (obj instanceof ModelResult<?>) {
                result = (ModelResult<RegistrationForm>) obj;
                result.success = true;
            }
            else {
                result.success = false;
                result.message = "Erreur de serveur. Veuillez contacter les administrateurs du serveur.";
            }
        } catch (IOException ex) {
            result.success = false;
            result.message = "Erreur de connexion au serveur. Veuillez réessayer plus tard.";
        } catch (ClassNotFoundException e) {
            result.success = false;
            result.message = "Erreur de serveur. Veuillez contacter les administrateurs du serveur.";
        }

        return result;
    }
}
