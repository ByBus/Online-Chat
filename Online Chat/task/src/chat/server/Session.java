package chat.server;

import chat.messaging.Receiver;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Session extends Thread {
    private final int clientId;
    private final Socket socket;

    public Session(int clientId, Socket socket) {
        this.clientId = clientId;
        this.socket = socket;
    }

    @Override
    public void run() {
        log(clientWithText("connected!"));

        try (DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            var receiver = new Receiver(input);
            receiver.setCallBack(message -> {
                if (message.startsWith("/exit")) {
                    log(clientWithText("disconnected!"));
                    receiver.interrupt();
                    socket.close();
                } else {
                    var countIs = "Count is " + countWords(message);
                    var countMessage = "Sent to client %d: %s".formatted(clientId, countIs);
                    output.writeUTF(countIs);
                    log(clientWithText("sent: " + message + "\n" + countMessage));
                }
            });
            receiver.start();
            receiver.join();
        } catch (InterruptedException | IOException ignored) {}
    }

    private String clientWithText(String text) {
        return "Client %d %s".formatted(clientId, text).trim();
    }

    private void log(String message) {
        System.out.println(message);
    }

    private int countWords(String message) {
        return message.trim().split("\\s+").length;
    }
}
