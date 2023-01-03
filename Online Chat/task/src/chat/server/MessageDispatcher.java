package chat.server;

import chat.server.data.MessagesRepository;
import chat.server.data.PasswordRepository;
import chat.server.model.Message;
import chat.server.model.User;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MessageDispatcher {
    private final MessagesRepository messagesRepository;
    private final PasswordRepository passwordRepository;
    private final Map<User, User> chats = new HashMap<>();
    private final Map<User, Consumer<String>> users = new HashMap<>();

    public MessageDispatcher(MessagesRepository messagesRepository,
                             PasswordRepository passwordRepository) {
        this.messagesRepository = messagesRepository;
        this.passwordRepository = passwordRepository;
    }

    public void saveMessagesToDisk() {
        try {
            messagesRepository.saveToDisk();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean addUser(User user, Consumer<String> respond) {
        if (users.containsKey(user)) return false;
        users.put(user, respond);
        return true;
    }

    public synchronized void syncUserChat(User author) {
        var addressee = chats.get(author);
        if (addressee != null) {
            Consumer<String> responder = users.get(author);
            var messages = messagesRepository.lastNMessages(author, addressee, 10);
            if (messages.isEmpty()) return;
            var messagesText = messages.stream()
                    .map(m -> m.prepareForUser(author))
                    .collect(Collectors.joining(System.lineSeparator()));
            messages.forEach(m -> m.markAsRead(author));
            responder.accept(messagesText);
        }
    }

    public synchronized boolean isOnline(User user, User addressee) {
        return chats.containsKey(addressee) && user.equals(chats.get(addressee));
    }

    public synchronized boolean isOnline(User addressee) {
        return users.containsKey(addressee);
    }

    public synchronized void addMessage(Message message) {
        var author = message.author();
        var addressee = message.addressee();
        if (isOnline(author, addressee)) {
            message.markAsRead(addressee);
            users.get(addressee).accept(message.toString());
        }
        message.markAsRead(author);
        users.get(author).accept(message.toString());

        messagesRepository.add(message);
    }

    public synchronized User currentTalkerOf(User user) {
        return chats.get(user);
    }

    public synchronized List<User> whoIsOnline() {
        return new ArrayList<>(users.keySet());
    }

    public synchronized void removeUser(User user) {
        chats.remove(user);
        users.remove(user);
    }


    public synchronized void makeChat(User currentUser, User secondUser) {
        try {
            if (passwordRepository.isRegistered(secondUser.toString())) {
                chats.put(currentUser, secondUser);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
