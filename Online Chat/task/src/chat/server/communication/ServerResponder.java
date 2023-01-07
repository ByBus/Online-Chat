package chat.server.communication;

import chat.server.model.Message;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class ServerResponder {
    private final DataOutputStream output;

    public ServerResponder(DataOutputStream output) {
        this.output = output;
    }

    public void send(String message)  {
        try {
            output.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(List<Message> messages) {
        if (messages.isEmpty()) return;
        var text = messages.stream()
                .map(Message::toString)
                .collect(Collectors.joining("\n"));
        send(text);
    }
}
