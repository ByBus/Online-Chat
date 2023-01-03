package chat.server;

import chat.server.command.Command;
import chat.server.exception.ExitException;
import chat.server.exception.RespondException;
import chat.server.model.User;
import chat.server.servicelocator.ServiceLocator;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

public class Session extends Thread {
    private final Socket socket;
    private User user;
    private boolean isRegistration = true;
    private ServerResponder responder;
    private Command command = ServiceLocator.provideAuthAndRegisterState(this);
    public Session(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            var receiver = new ServerReceiver(input);
            responder = new ServerResponder(output);
            responder.send("Server: authorize or register");

            receiver.setCallBack(message -> {
                try {
                    var respond = command.execute(message);
                    if (!respond.isBlank()) responder.send(respond);
                    if (isRegistration) { // if no exceptions above this line then user logged in
                        command = ServiceLocator.provideConversationState(user);
                        isRegistration = false;
                    }
                } catch (RespondException e) {
                    responder.send(e.getMessage());
                } catch (ExitException e) {
                    receiver.interrupt();
                    socket.close();
                }
            });
            receiver.start();
            receiver.join();
        } catch (InterruptedException | IOException ignored) {
        }
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Consumer<String> respond() {
        return responder::send;
    }
}
