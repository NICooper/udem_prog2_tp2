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

public class Client {
    static ClientController controller;
    static List<Course> courses;

    public static void main(String[] args)  {
        controller = new ClientController(new RemoteCourseList(), new RemoteCourseRegistration());

        System.out.println("*** Bienvenue au portail d'inscription de cours de l'UDEM ***");
        getAvailableCourses();
    }

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