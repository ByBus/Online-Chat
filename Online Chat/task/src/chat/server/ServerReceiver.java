package chat.server;

import java.io.DataInputStream;
import java.io.IOException;

public class ServerReceiver extends Thread {
    private final DataInputStream inputStream;
    private Callback callback;

    public ServerReceiver(DataInputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            try {
                String message = inputStream.readUTF();
                if (callback == null) {
                    System.out.println(message);
                } else {
                    callback.execute(message);
                }
            } catch (IOException ignored) {
            }
        }
    }

    public void setCallBack(Callback callback) {
        this.callback = callback;
    }

    public interface Callback {
        void execute(String message) throws IOException;
    }
}