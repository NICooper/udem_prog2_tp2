package views.client_simple;

import controllers.ClientController;
import shared.models.Course;
import shared.models.RegistrationForm;
import shared.models.RemoteCourseList;
import shared.models.RemoteCourseRegistration;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Le programme qui se connecte au serveur pour lui envoyer des requêtes
 */
public class Client {
    static ClientController controller;
    static List<Course> courses;

    /**
     * Imprime le message de bienvenue et démarre les deux methods getAvailableCourses() et
     * inscriptionToCourse().
     * @param args pas utilisé
     */
    public static void main(String[] args)  {
        controller = new ClientController(new RemoteCourseList(), new RemoteCourseRegistration());

        System.out.println("*** Bienvenue au portail d'inscription de cours de l'UDEM ***");
        getAvailableCourses();
    }

    /**
     * Affiche le message au client pour choisir la session et récupère la liste disponible
     * pour une session donnée, après affiche la liste des cours sous le format donné.
     * courses est la liste des cours retournée par le Server.
     * n'a pu être trouvé
     */
    private static void getAvailableCourses() {
        System.out.println("Veuillez choisir la session pour laquelle vous voulez consulter la liste des cours:");
        System.out.println("1. Automne\n2. Hiver\n3. Ete");
        System.out.println("Choix: ");

        Scanner scannerSession = new Scanner(System.in);
        int sessionNo = scannerSession.nextInt();
        scannerSession.nextLine();
        String session = null;
        if (sessionNo == 1) {
            session = "Automne";
        } else if (sessionNo == 2) {
            session = "Hiver";
        } else if (sessionNo == 3) {
            session = "Ete";
        } else {
            System.out.println("Entrée invalide");
        }

        controller.setSession(session, (sess, validation) -> {});
        controller.loadCourseList((result) -> {
            courses = result.data;
        });

        System.out.println("Les cours offerts pendant la session d'" + session + " sont:");

        for (int i = 0; i < courses.size(); i++) {
            System.out.println(i + 1 + ". " + courses.get(i).getCode() +
                    " " + courses.get(i).getName());
        }

        inscriptionToCourse();
    }

    /**
     * Récupère les données nécessaires afin de s'inscrire à un cours.
     * Donne le choix de consulter une autre session ou de faire l'inscription au client. Si le client
     * choisit de s'inscrire au cours, il doit donner les informations requises, y compris le code du cours.
     * Le code du cours doit être présent dans la liste des cours disponibles dans la session en question.
     * Après le client va envoyer toutes les informations sous la forme d'un Objet RegistrationForm.
     * n'a pu être trouvé
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
            getAvailableCourses();
        } else if (inscription != 2) {
            System.out.println("Entrée invalide");
            inscriptionToCourse();
        }

        System.out.println();
        System.out.println("Veuillez saisir votre prénom: ");
        String firstName = scanner.nextLine();
        controller.setFirstName(firstName, (s, v) -> {});

        System.out.println("Veuillez saisir votre nom: ");
        String lastName = scanner.nextLine();
        controller.setLastName(lastName, (s, v) -> {});

        System.out.println("Veuillez saisir votre email: ");
        String email = scanner.nextLine();
        controller.setEmail(email, (s, v) -> {});

        System.out.println("Veuillez saisir votre matricule: ");
        String matricule = scanner.nextLine();
        controller.setMatricule(matricule, (s, v) -> {});

        System.out.println("Veuillez saisir le code du cours: ");
        String code = scanner.nextLine();

        Course matchedCourse = null;
        for (Course course: courses) {
            if (course.getCode().equals(code)) {
                matchedCourse = course;
                break;
            }
        }
        if (matchedCourse == null) {
            System.out.println("Cours introuvable");
            getAvailableCourses();
        }

        controller.setCourse(matchedCourse, (c, v) -> {});

        controller.registerToCourse((result) -> {
            System.out.println(result.message);
            getAvailableCourses();
        });
    }
}