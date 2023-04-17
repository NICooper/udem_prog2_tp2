package server;

import javafx.util.Pair;
import shared.models.Course;
import shared.models.ModelResult;
import shared.models.RegistrationForm;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * Le programme qui attend de recevoir des commandes pour s'exécuter
 */
public class Server {
    /**
     * La commande pour s'inscrire au cours
     */
    public final static String REGISTER_COMMAND = "INSCRIRE";
    /**
     * La commande pour charger les cours correspondant aux critères donnés
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
     * @throws IOException Exception produite par des opérations d'entrée/sortie échouées ou interrompues
     */
    public Server(int port) throws IOException {
        this.server = new ServerSocket(port, 1);
        this.handlers = new ArrayList<EventHandler>();
        this.addEventHandler(this::handleEvents);
    }

    /**
     * Ajoute les EventHandlers à la liste handlers contenant les méthodes qui réagissent aux
     * différents événements
     * @param h Object EventHandler
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
     * Interprète la commande du client et averti tous les handlers lorsqu'un événement
     * survient
     * @throws IOException erreur d'entrée du stream
     * @throws ClassNotFoundException erreur quand aucune définition de la classe alertHandlers
     * n'a pu être trouvée.
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
     *
     * La coupe du line est effectuée au premier espace
     * @param line le String de commande reçue
     * @return Le nouvel objet Pair qui contient le type de commande et l'argument précise.
     */
    public Pair<String, String> processCommandLine(String line) {
        String[] parts = line.split(" ");
        String cmd = parts[0];
        String args = String.join(" ", Arrays.asList(parts).subList(1, parts.length));
        return new Pair<>(cmd, args);
    }

    /**
     * Ferme l'objet Socket connecté au client après la requête soit exécutée
     * @throws IOException erreur d'entrée/sortie
     */
    public void disconnect() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        client.close();
    }

    /**
     * Appelle la fonction correspondante selon la requête reçue
     * @param cmd le type de commande
     * @param arg l'argument précise
     */
    public void handleEvents(String cmd, String arg) {
        if (cmd.equals(REGISTER_COMMAND)) {
            handleRegistration();
        } else if (cmd.equals(LOAD_COMMAND)) {
            handleLoadCourses(arg);
        }
    }

    /**
     * Charge la liste des cours filtrée par la session demandée par le client.
     *
     * Le fichier cours.txt contient tous les cours disponibles que le serveur doit lire.
     * La liste courses contient les cours sous le format de Class Course, filtré par la session
     * avec le code, le nom et la session du cours.
     *
     * @param session la session pour laquelle on veut récupérer la liste des cours
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
     * Inscrit le cours demandé par le client et récupère les informations du client.
     * Si le code du cours demandé existe dans la liste du cours disponible dans la session demandée,
     * le serveur ajoutera les informations relatives au client et au cours dans le fichier
     * inscription.txt sous le format requis. Après, un message de réussite (ou celui d'échec)
     * est envoyé au client.
     *
     * La liste courses contient tous les cours disponibles sous le format Object Course.
     * L'objet InscriptionInfo contient les informations d'inscription au cours.
     * Le String clientInscription est la ligne d'inscription correspondante au fichier inscription.txt.
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
            System.out.println("Erreur de lecture de la classe RegistrationForm");
        }
    }
}
