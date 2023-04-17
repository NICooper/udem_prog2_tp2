package shared.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 */
public class RemoteCourseRegistration extends CourseRegistration {
    private String ipAddress;
    private int port;
    private String serverCommand;

//    public RemoteCourseRegistration() {
//        super();
//    }

//    public RemoteCourseRegistration(RegistrationForm form) {
//        super(form);
//    }

    /**
     *
     * @param ipAddress
     * @param port
     * @param serverCommand
     */
    public RemoteCourseRegistration(String ipAddress, int port, String serverCommand) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.serverCommand = serverCommand;
    }

    /**
     *
     * @return
     */
    @Override
    public ModelResult<RegistrationForm> register() {
        ModelResult<RegistrationForm> result = new ModelResult<>();

        try (Socket clientSocket = new Socket(this.serverCommand, this.port);
             ObjectOutputStream os = new ObjectOutputStream(clientSocket.getOutputStream());
             ObjectInputStream is = new ObjectInputStream(clientSocket.getInputStream())
        ) {
            os.writeObject(this.serverCommand);

            os.writeObject(this.validatedForm.form);


            Object obj = is.readObject();
            if (obj instanceof ModelResult<?>) {
                result = (ModelResult<RegistrationForm>) obj;
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
