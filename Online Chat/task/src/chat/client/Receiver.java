package chat.client;

import java.io.DataInputStream;
import java.io.IOException;

public class Receiver extends Thread {
    private final DataInputStream inputStream;

    public Receiver(DataInputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                String message = inputStream.readUTF();
                System.out.println(message);
            } catch (IOException ignored) {
            }
        }
    }
}

