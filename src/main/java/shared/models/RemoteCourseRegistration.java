package shared.models;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class RemoteCourseRegistration extends CourseRegistration {
    public RemoteCourseRegistration() {
        super();
    }

    public RemoteCourseRegistration(RegistrationForm form) {
        super(form);
    }

    @Override
    public ModelResult<RegistrationForm> register() {
        ModelResult<RegistrationForm> result = new ModelResult<>();

        try {
            Socket clientSocket = new Socket("127.0.0.1", 1337);
            ObjectOutputStream os = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream is = new ObjectInputStream(clientSocket.getInputStream());

            os.writeObject("INSCRIRE");

            os.writeObject(this.form);


            Object obj = is.readObject();
            if (obj instanceof ModelResult<?>) {
                result = (ModelResult<RegistrationForm>) obj;
            }
            else {
                result.success = false;
                result.message = "Erreur de serveur. Veuillez contacter les administrateurs du serveur.";
            }

            is.close();
            os.close();

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
