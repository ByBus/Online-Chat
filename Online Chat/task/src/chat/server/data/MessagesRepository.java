package chat.server.data;

import chat.server.model.Message;
import chat.server.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MessagesRepository {
    private final Serializer<List<Message>> serializer;
    private final List<Message> messages;

    public MessagesRepository(Serializer<List<Message>> serializer) {
        this.serializer = serializer;
        messages = new ArrayList<>(read());
    }

    public void saveToDisk() throws IOException {
        serializer.serialize(messages);
    }

    public void add(Message message) {
        messages.add(message);
    }

    public List<Message> lastNMessages(User author, User addressee, int n) {
        var filtered = messages.stream()
                .filter(m -> m.isChatting(author) && m.isChatting(addressee))
                .toList();

        return (filtered.size() > n)
                ? filtered.subList(filtered.size() - n, filtered.size())
                : filtered.stream().toList();
    }

    public List<Message> read() {
        List<Message> messages = Collections.emptyList();
        try {
            messages = serializer.deserialize();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("MessagesDb file not yet created.");
           // e.printStackTrace();
        }
        return messages;
    }
}
