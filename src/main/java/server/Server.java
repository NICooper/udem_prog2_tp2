package server;

import javafx.util.Pair;
import shared.models.*;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Le serveur de l'inscription de UdeM. Permet aux clients de consulter la liste de cours de chaque session
 * et de s'inscrire aux cours.
 */
public class Server {
    /**
     * La commande pour s'inscrire au cours
     */
    public final static String REGISTER_COMMAND = "INSCRIRE";
    /**
     * La commande pour charger la liste de cours
     */
    public final static String LOAD_COMMAND = "CHARGER";
    private final ServerSocket server;
    private Socket client;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private final ArrayList<EventHandler> handlers;

    /**
     * Crée un objet Server sur le port spécifié et une liste de tous les EventHandlers ajoutés.
     * @param port Porte écoutée par le serveur
     * @throws IOException Exception qui peut être lancée lors de la création du Socket du serveur
     */
    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    /**
     * Ajoute les EventHandlers à la liste des handlers qui seront appelés quand le serveur reçoit une commande d'un client
     * @param h Un EventHandler.
     */
    public void addEventHandler(EventHandler h) {
        this.handlers.add(h);
    }

    /**
     * Appelle tous les EventHandlers qui sont abonnés aux évènements du Server.
     * @param cmd Commande donnée par le client
     * @param arg Argument spécifié par le client
     */
    private void alertHandlers(String cmd, String arg) {
        for (EventHandler h : this.handlers) {
            h.handle(cmd, arg);
        }
    }

    /**
     *  Attend la connexion d'un client, écoute et exécute les requêtes envoyées par le client,
     *  et déconnecte une fois la requête est traitée.
     */
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

    /**
     * Écoute la commande du client et averti tous les handlers lorsqu'une commande est reçu
     * @throws IOException erreur du stream utilisé pour reçevoir des données du client
     * @throws ClassNotFoundException erreur quand la classe de l'objet envoyé par le client n'est pas compris par le serveur
     */
    public void listen() throws IOException, ClassNotFoundException {
        String line;
        if ((line = this.objectInputStream.readObject().toString()) != null) {
            Pair<String, String> parts = processCommandLine(line);
            String cmd = parts.getKey();
            String arg = parts.getValue();
            this.alertHandlers(cmd, arg);
        }
    }

    /**
     * Découpe la commande reçue sous la forme d'un String, et renvoie un nouveau Pair.
     * La coupe du line est effectuée à la première espace
     * @param line le String de commande reçue
     * @return Le nouvel objet Pair qui contient le type de commande et l'argument pour cette commande.
     */
    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    /**
     * Ferme l'objet Socket connecté au client après que la requête soit exécutée
     * @throws IOException erreur d'entrée/sortie qui peut arriver en fermant la connexion au client
     */
    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

    /**
     * Appelle la fonction correspondante selon la commande reçue
     * @param cmd le nom de la commande
     * @param arg un argument à donner à la fonction qui correspond à la commande
     */
    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     * Charge la liste des cours filtré par la session demandée par le client.
     * Le fichier cours.txt contient tous les cours disponible et est dans le format tsv:
     * code nom session
     * @param arg la session pour laquelle on veut récupérer la liste des cours
     */
    public void handleLoadCourses(String arg) {
        LocalCourseList courseList = new LocalCourseList("cours.txt");
        DataValidation sessionValidation = courseList.setSessionFilter(arg);

        ModelResult<List<Course>> result = new ModelResult<>();

        if (sessionValidation.isValid) {
            result = courseList.loadFilteredCourseList();
        }
        else {
            result.success = false;
            result.message = sessionValidation.validationMessage;
        }

        try {
            this.objectOutputStream.writeObject(result);
        }
        catch (IOException e) {
            System.out.println("Une erreur est survenue en répondant au client.");
        }
    }

    /**
     * Inscrit le cours demandé par le client et récupère les informations du client.
     * Si le code du cours demandé existe dans la liste du cours disponible dans la session demandée,
     * le serveur ajoutera les informations relatives au client et au cours dans le fichier
     * inscription.txt. Après, un message de réussite (ou celui d'échec) est envoyé au client.
     */
    public void handleRegistration() {
        LocalCourseList courseList = new LocalCourseList("cours.txt");
        ModelResult<List<Course>> coursesResult = courseList.loadFilteredCourseList();

        ModelResult<RegistrationForm> result = new ModelResult<>();

        if (!coursesResult.success) {
            result.success = false;
            result.message = coursesResult.message;
        }
        else {
            List<Course> courses = coursesResult.data;

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
                    LocalCourseRegistration courseRegistration = new LocalCourseRegistration("inscription.txt");
                    DataValidation formValidation = courseRegistration.getValidatedForm().setRegistrationForm(inscriptionInfo);
                    if (formValidation.isValid) {
                        ModelResult<RegistrationForm> writeResult = courseRegistration.register();
                        if (writeResult.success) {
                            result.data = inscriptionInfo;
                            result.success = true;
                            result.message = writeResult.message;
                        }
                        else {
                            result.success = false;
                            result.message = "Le serveur n'a pas pu sauvegarder l'inscription.";
                            System.out.println("Erreur à l'écriture du fichier");
                        }
                    } else {
                        result.success = false;
                        result.message = "Le formulaire d'inscription fourni n'est pas valide.";
                        System.out.println("Le formulaire d'inscription fourni n'est pas valide.");
                    }
                } else {
                    result.success = false;
                    result.message = "Cours introuvable du côté serveur.";
                }

            } catch (ClassNotFoundException ex) {
                result.success = false;
                result.message = "Erreur de communcations entre le serveur et client.";
                System.out.println("L'objet envoyé par le client n'est pas connu par le serveur.");
            } catch (IOException ex) {
                result.success = false;
                result.message = "Erreur de communcations entre le serveur et client.";
                System.out.println("Erreur de stream de données entre le client et serveur.");
            }
        }

        try {
            this.objectOutputStream.writeObject(result);
        }
        catch (IOException e) {
            System.out.println("Erreur survenue en répondant au client.");
        }
    }
}
