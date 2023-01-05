package chat.server;


import chat.server.servicelocator.ServiceLocator;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    public static void main(String[] args) {
        System.out.println("Server started!");
        try (ServerSocket server = new ServerSocket(23456)) {
            server.setSoTimeout(5000);
            while (true) {
                var session = new Session(server.accept());
                session.start();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            ServiceLocator.provideMessageDispatcher().saveDataToDisk();
        }
    }
}