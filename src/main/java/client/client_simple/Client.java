package client.client_simple;

import models.Course;
import models.RegistrationForm;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
    static ArrayList<Course> courses;

    public static void main(String[] args)  {

        System.out.println("*** Bienvenue au portail d'inscription de cours de l'UDEM ***");
        getAvailableCourses();
    }

    public static void getAvailableCourses() {
        System.out.println("Veuillez choisir la session pour laquelle vous voulez consulter la liste des cours:");
        System.out.println("1. Automne\n2. Hiver\n3. Ete");
        System.out.println("Choix: ");

        Scanner scannerSession = new Scanner(System.in);
        int sessionNo = scannerSession.nextInt();
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

        try {
            Socket clientSocket = new Socket("127.0.0.1", 1337);
            ObjectOutputStream os = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream is = new ObjectInputStream(clientSocket.getInputStream());

            os.writeObject("CHARGER " + session);

            System.out.println("Les cours offerts pendant la session d'" + session + " sont:");

            Object response = is.readObject();
            if (response instanceof ArrayList<?>) {
                courses = (ArrayList<Course>) response;
                for (int i = 0; i < courses.size(); i++) {
                    System.out.println(i + 1 + ". " + courses.get(i).getCode() +
                            " " + courses.get(i).getName());
                }
            }
            is.close();
            os.close();
            inscriptionToCourse();

        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public static void inscriptionToCourse() {
        System.out.println("Choix:");
        System.out.println("1. Consulter les cours offerts pour une autre session");
        System.out.println("2. Inscription à un cours");
        System.out.println("Choix:");

        Scanner scannerInscription = new Scanner(System.in);
        int inscription = scannerInscription.nextInt();

        if (inscription == 1) {
            getAvailableCourses();
        } else if (inscription != 2) {
            System.out.println("Entrée invalide");
            inscriptionToCourse();
        }

        System.out.println(" ");
        System.out.println("Veuillez saisir votre prénom: ");
        Scanner scannerFN = new Scanner(System.in);
        String firstName = scannerFN.nextLine();

        System.out.println("Veuillez saisir votre nom: ");
        Scanner scannerLN = new Scanner(System.in);
        String lastName = scannerLN.nextLine();

        System.out.println("Veuillez saisir votre email: ");
        Scanner scannerEmail = new Scanner(System.in);
        String email = scannerEmail.nextLine();

        System.out.println("Veuillez saisir votre matricule: ");
        Scanner scannerMatricule = new Scanner(System.in);
        String matricule = scannerMatricule.nextLine();

        System.out.println("Veuillez saisir le code du cours: ");
        Scanner scannerCode = new Scanner(System.in);
        String code = scannerCode.nextLine();
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


        try {
            Socket clientSocket = new Socket("127.0.0.1", 1337);
            ObjectOutputStream os = new ObjectOutputStream(clientSocket.getOutputStream());
            ObjectInputStream is = new ObjectInputStream(clientSocket.getInputStream());

            os.writeObject("INSCRIRE");
            RegistrationForm inscriptionInfo = new RegistrationForm(
                    firstName, lastName, email, matricule, matchedCourse);
            os.writeObject(inscriptionInfo);

            System.out.println(is.readObject());

            is.close();
            os.close();

        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}