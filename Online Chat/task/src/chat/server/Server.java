package chat.server;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    public static void main(String[] args) {
        System.out.println("Server started!");
        int nextClientId = 1;

        try (ServerSocket server = new ServerSocket(23456)) {
            server.setSoTimeout(1000);
            while (true) {
                var session = new Session(nextClientId++, server.accept());
                session.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}