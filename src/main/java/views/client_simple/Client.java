package views.client_simple;

import controllers.ClientController;
import shared.models.Course;
import shared.models.RemoteCourseList;
import shared.models.RemoteCourseRegistration;

import java.util.List;
import java.util.Scanner;

/**
 * Un client sur la ligne de commande qui sert à consulter les cours de UdeM et a s'y inscrire.
 */
public class Client {
    private static String serverIpAddress = "127.0.0.1";
    private static int serverPort = 1337;
    private static ClientController controller;
    private static List<Course> courses;

    /**
     * Imprime le message de bienvenue et démarre les deux méthodes getAvailableCourses() et
     * inscriptionToCourse().
     * @param args pas utilisé
     */
    public static void main(String[] args)  {
        controller = new ClientController(
                new RemoteCourseList(serverIpAddress, serverPort, "CHARGER"),
                new RemoteCourseRegistration(serverIpAddress, serverPort, "INSCRIRE")
        );

        System.out.println("*** Bienvenue au portail d'inscription de cours de l'UDEM ***");
        while (true) {
            getAvailableCourses();
        }
    }

    /**
     * Affiche le message au client pour choisir la session et récupère la liste de cours disponibles
     * pour une session donnée, après affiche la liste des cours à l'utilisateur.
     */
    private static void getAvailableCourses() {
        System.out.println();
        System.out.println("Veuillez choisir la session pour laquelle vous voulez consulter la liste des cours:");
        System.out.println("1. Automne\n2. Hiver\n3. Ete");
        System.out.println("Choix: ");

        Scanner scannerSession = new Scanner(System.in);
        String sessionLine = scannerSession.nextLine();
        String session = null;
        if (sessionLine.equals("1")) {
            session = "Automne";
        } else if (sessionLine.equals("2")) {
            session = "Hiver";
        } else if (sessionLine.equals("3")) {
            session = "Ete";
        } else {
            System.out.println("Entrée invalide");
            return;
        }

        controller.setSession(session, (sess, validation) -> {});
        controller.loadCourseList((result) -> {
            if (!result.success) {
                System.out.println(result.message);
                getAvailableCourses();
            }
            courses = result.data;
        });

        System.out.println("Les cours offerts pendant la session d'" + session + " sont:");

        for (int i = 0; i < courses.size(); i++) {
            System.out.println(i + 1 + ". " + courses.get(i).getCode() + " " + courses.get(i).getName());
        }

        inscriptionToCourse();
    }

    /**
     * Récupère les données nécessaires afin de s'inscrire à un cours.
     * Donne le choix de consulter une autre session ou de faire l'inscription au client. Si le client
     * choisit de s'inscrire au cours, il doit donner les informations requises, y compris le code du cours.
     * Le code du cours doit être présent dans la liste des cours disponibles dans la session en question.
     * Après le client va envoyer toutes les informations sous la forme d'un Objet RegistrationForm.
     */
    private static void inscriptionToCourse() {
        System.out.println("Choix:");
        System.out.println("1. Consulter les cours offerts pour une autre session");
        System.out.println("2. Inscription à un cours");
        System.out.println("Choix:");

        Scanner scanner = new Scanner(System.in);
        int inscription = scanner.nextInt();
        scanner.nextLine();

        if (inscription == 1) {
            return;
        } else if (inscription != 2) {
            System.out.println("Entrée invalide");
            inscriptionToCourse();
        }

        System.out.println();
        System.out.println("Veuillez saisir votre prénom: ");
        String firstName = scanner.nextLine();
        controller.setFirstName(firstName, (s, validation) -> {
            if (!validation.isValid) {
                System.out.println(validation.validationMessage);
                inscriptionToCourse();
            }
        });

        System.out.println("Veuillez saisir votre nom: ");
        String lastName = scanner.nextLine();
        controller.setLastName(lastName, (s, validation) -> {
            if (!validation.isValid) {
                System.out.println(validation.validationMessage);
                inscriptionToCourse();
            }
        });

        System.out.println("Veuillez saisir votre email: ");
        String email = scanner.nextLine();
        controller.setEmail(email, (s, validation) -> {
            if (!validation.isValid) {
                System.out.println(validation.validationMessage);
                inscriptionToCourse();
            }
        });

        System.out.println("Veuillez saisir votre matricule: ");
        String matricule = scanner.nextLine();
        controller.setMatricule(matricule, (s, validation) -> {
            if (!validation.isValid) {
                System.out.println(validation.validationMessage);
                inscriptionToCourse();
            }
        });

        System.out.println("Veuillez saisir le code du cours: ");
        String code = scanner.nextLine();

        controller.setCourseByCode(code, (course, validation) -> {
            if (validation.isValid) {
                controller.registerToCourse((result) -> {
                    System.out.println(result.message);
                });
            }
            else {
                System.out.println(validation.validationMessage);
                inscriptionToCourse();
            }
        });
    }
}