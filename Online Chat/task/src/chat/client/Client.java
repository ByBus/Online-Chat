package chat.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 23456;

    public static void main(String[] args) throws InterruptedException {
        Thread.sleep(100L);
        System.out.println("Client started!");

        try (Socket socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            var receiver = new Receiver(input);
            receiver.start();

            var responder = new Responder(output);
            responder.run(); //run on main

            receiver.interrupt();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
