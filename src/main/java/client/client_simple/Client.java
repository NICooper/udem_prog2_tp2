package client.client_simple;

import models.Course;
import models.RegistrationForm;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {

        System.out.println("*** Bienvenue au portail d'inscription de cours de l'UDEM ***");

        public void getAvailableCourses() {
            System.out.println("Veuillez choisir la session pour laquelle vous voulez consulter la liste des cours:");
            System.out.println("1. Automne\n2. Hiver\n3. Ete");
            System.out.println("Choix: ");

            try {
                Socket clientSocket = new Socket("127.0.0.1", 1337);
                ObjectInputStream is = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream os = new ObjectOutputStream(clientSocket.getOutputStream());

                Scanner scannerSession = new Scanner(System.in);
                int sessionNo = scannerSession.nextInt();
                if (sessionNo == 1) {
                    String session = "Automne";
                } else if (sessionNo == 2) {
                    String session = "Hiver";
                } else if (sessionNo == 3) {
                    String session = "Ete";
                } else {
                    System.out.println("Entrée invalide");
                }
//              handleLoadCourses(session)??



                System.out.println("Les cours offerts pendant la session d'automne sont:");


                System.out.println("Choix:");
                System.out.println("1. Consulter les cours offerts pour une autre session");
                System.out.println("2. Inscription à un cours");
                System.out.println("Choix:");

                Scanner scannerInscription = new Scanner(System.in);
                int inscription = scannerInscription.nextInt();

                if (inscription == 1) {
                    // Appeler le method getAvailableCourses
                } else if (inscription == 2) {
                    // Appeler le method inscriptionToCourse
                } else {
                    System.out.println("Entrée invalide");
                }


            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public void inscriptionToCourse() {

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
            int matricule = scannerMatricule.nextInt();

            System.out.println("Veuillez saisir le code du cours: ");
            Scanner scannerCode = new Scanner(System.in);
            String code = scannerCode.nextLine();


//            handleRegistration()??


//            while(scanner.hasNext()) {
//                var sess = scanner.nextLine();
//                os.writeObject("CHARGER " + sess);
//                System.out.println(is.readObject());
//            }

            System.out.println(clientSocket.isConnected());
//            ObjectInputStream is = new ObjectInputStream(
//                    clientSocket.getInputStream()
//            );

            Object response = is.readObject();

            System.out.println(response.toString());
            is.close();
            os.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
