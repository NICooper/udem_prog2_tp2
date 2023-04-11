package server;

import javafx.util.Pair;
import shared.models.Course;
import shared.models.ModelResult;
import shared.models.RegistrationForm;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {

    public final static String REGISTER_COMMAND = "INSCRIRE";
    public final static String LOAD_COMMAND = "CHARGER";
    private final ServerSocket server;
    private Socket client;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final ArrayList<EventHandler> handlers;

    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    public void run() {
        while (true) {
            try {
                client = server.accept();
                System.out.println("Connecté au client: " + client);
                objectInputStream = new ObjectInputStream(client.getInputStream());
                objectOutputStream = new ObjectOutputStream(client.getOutputStream());
                listen();
                disconnect();
                System.out.println("Client déconnecté!");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void listen() throws IOException, ClassNotFoundException {
        String line;
        if ((line = this.objectInputStream.readObject().toString()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }

    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     * Lire un fichier texte contenant des informations sur les cours et les transofmer en liste d'objets 'Course'.
     * La méthode filtre les cours par la session spécifiée en argument.
     * Ensuite, elle renvoie la liste des cours pour une session au client en utilisant l'objet 'objectOutputStream'.
     * La méthode gère les exceptions si une erreur se produit lors de la lecture du fichier ou de l'écriture de l'objet dans le flux.
     *
     * @param arg la session pour laquelle on veut récupérer la liste des cours
     */
    public void handleLoadCourses(String arg) {
        try (FileReader fr = new FileReader("cours.txt")) {
            BufferedReader reader = new BufferedReader(fr);

            List<Course> courses = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] courseInfo = line.split("\t");
                if (courseInfo[2].equals(arg)) {
                    courses.add(new Course(courseInfo[1], courseInfo[0], courseInfo[2]));
                }
            }
            this.objectOutputStream.writeObject(courses);

        } catch (FileNotFoundException fe) {
            System.out.println("Fichier pas trouvé.");
        } catch (IOException ex) {
            System.out.println("Erreur à l'ouverture du fichier.");
        }
    }

    /**
     * Récupérer l'objet 'RegistrationForm' envoyé par le client en utilisant 'objectInputStream',
     * l'enregistrer dans un fichier texte
     * et renvoyer un message de confirmation au client.
     * La méthode gére les exceptions si une erreur se produit lors de la lecture de l'objet,
     * l'écriture dans un fichier ou dans le flux de sortie.
     */
    public void handleRegistration() {

        List<Course> courses = new ArrayList<>();
        ModelResult<RegistrationForm> result = new ModelResult<>();

        try (FileReader fr = new FileReader("cours.txt")) {
            BufferedReader reader = new BufferedReader(fr);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] courseInfo = line.split("\t");
                courses.add(new Course(courseInfo[1], courseInfo[0], courseInfo[2]));
            }
        } catch (FileNotFoundException fe) {
            System.out.println("Fichier pas trouvé.");
        } catch (IOException ex) {
            System.out.println("Erreur à l'ouverture du fichier.");
        }

        try {
            RegistrationForm inscriptionInfo = (RegistrationForm) objectInputStream.readObject();

            boolean hasMatchingCourse = false;
            for (Course cours : courses) {
                if (cours.getCode().equals(inscriptionInfo.getCourse().getCode())
                        && (cours.getSession().equals(inscriptionInfo.getCourse().getSession()))) {
                    hasMatchingCourse = true;
                    break;
                }
            }
            if (hasMatchingCourse) {
                String clientInscription = inscriptionInfo.getCourse().getSession() + "\t"
                        + inscriptionInfo.getCourse().getCode() + "\t" + inscriptionInfo.getMatricule()
                        + "\t" + inscriptionInfo.getPrenom() + "\t" + inscriptionInfo.getNom()
                        + "\t" + inscriptionInfo.getEmail() + "\n";
                try {
                    FileWriter fw = new FileWriter("inscription.txt", true);
                    BufferedWriter writer = new BufferedWriter(fw);
                    writer.append(clientInscription);
                    writer.close();
                    result.success = true;
                    result.message = "Félicitations! Inscription réussie de "
                            + inscriptionInfo.getPrenom() + " au cours "
                            + inscriptionInfo.getCourse().getCode() +".";
                } catch (IOException e) {
                    System.out.println("Erreur à l'écriture du fichier");
                }
            } else {
                result.success = false;
                result.message = "Cours introuvable.";
            }
            this.objectOutputStream.writeObject(result);
        } catch (ClassNotFoundException ex) {
            System.out.println("La class lue n'existe pas dans le programme");
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Erreur à la lecture du fichier");
        }
    }
}
