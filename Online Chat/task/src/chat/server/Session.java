package chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Session extends Thread {
    private static final String NAME_ALREADY_TAKEN = "Server: this name is already taken! Choose another one.";
    private static final String WRITE_NAME = "Server: write your name";
    private final MessageDispatcher messageDispatcher;
    private final Socket socket;
    private User user;

    public Session(MessageDispatcher messageDispatcher, Socket socket) {
        this.messageDispatcher = messageDispatcher;
        this.socket = socket;
    }

    @Override
    public void run() {
        try (DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            var receiver = new ServerReceiver(input);
            var responder = new ServerResponder(output);
            responder.send(WRITE_NAME);

            receiver.setCallBack(message -> {
                if (message.startsWith("/exit")) {
                    messageDispatcher.removeUser(user);
                    receiver.interrupt();
                    socket.close();
                    return;
                }
                if (user == null) {
                    user = new User(message, responder::send);
                    if (!messageDispatcher.addUser(user)) {
                        responder.send(NAME_ALREADY_TAKEN);
                        user = null;
                    }
                    return;
                }
                messageDispatcher.addMessage(new Message(user, message));
            });
            receiver.start();
            receiver.join();
        } catch (InterruptedException | IOException ignored) {
        }
    }
}
