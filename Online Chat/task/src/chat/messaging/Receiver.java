package chat.messaging;

import java.io.DataInputStream;
import java.io.IOException;

public class Receiver extends Thread{
    private final DataInputStream inputStream;
    private Callback callback;

    public Receiver(DataInputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                String message = inputStream.readUTF();
                if (callback != null) {
                    callback.execute(message);
                } else {
                    System.out.println(message);
                }
            } catch (IOException ignored) { }
        }
    }

    public void setCallBack(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void execute(String message) throws IOException;
    }
}