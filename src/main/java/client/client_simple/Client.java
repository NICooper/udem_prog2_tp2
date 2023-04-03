package client.client_simple;

import models.Course;
import models.RegistrationForm;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        try {
            Socket clientSocket = new Socket("127.0.0.1", 1337);

            ObjectOutputStream os = new ObjectOutputStream(
                clientSocket.getOutputStream()
            );
            Scanner scanner = new Scanner(System.in);

//            while(scanner.hasNext()) {
//
//
//                writer.flush();
//            }
            os.writeObject("INSCRIRE");
            os.writeObject(new RegistrationForm("a", "b", "ab@gmail.com", "123", new Course("Prog 2", "IFT2255", "Automne")));
//            os.writeObject("CHARGER Hiver");


            System.out.println(clientSocket.isConnected());
            ObjectInputStream is = new ObjectInputStream(
                    clientSocket.getInputStream()
            );

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
