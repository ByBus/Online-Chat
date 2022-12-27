package chat.client;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;


public class Responder extends Thread {
    private final DataOutputStream output;

    public Responder(DataOutputStream output) {
        this.output = output;
    }

    @Override
    public void run() {
        var scanner = new Scanner(System.in);
        while (!Thread.currentThread().isInterrupted()) {
            String msg = scanner.nextLine();
            try {
                output.writeUTF(msg);
            } catch (IOException ignored) {
            } finally {
                if (msg.startsWith("/exit")) interrupt();
            }
        }
    }
}
