package client.client_simple;

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
            os.writeObject("CHARGER Hiver");
            os.close();

            System.out.println(clientSocket.isConnected());
            ObjectInputStream is = new ObjectInputStream(
                    clientSocket.getInputStream()
            );

            Object response;
            while ((response = is.readObject()) == null) {

            }

            System.out.println(response.toString());
            is.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
