package chat.server.communication;

import chat.server.data.MessagesRepository;
import chat.server.data.RegistrationRepository;
import chat.server.model.Message;
import chat.server.model.User;

import java.io.IOException;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class MessageDispatcher {
    private final MessagesRepository messagesRepository;
    private final RegistrationRepository registrationRepository;
    private final Map<User, User> chats = new HashMap<>();
    private final Map<User, Communication> users = new HashMap<>();

    public MessageDispatcher(MessagesRepository messagesRepository,
                             RegistrationRepository registrationRepository) {
        this.messagesRepository = messagesRepository;
        this.registrationRepository = registrationRepository;
    }

    public void saveDataToDisk() {
        try {
            messagesRepository.saveToDisk();
            registrationRepository.saveToDisk();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void addUser(User user, Communication respond) {
        if (users.containsKey(user)) return;
        users.put(user, respond);
    }

    public synchronized void lastMessages(User author, int quantity) {
        var addressee = chats.get(author);
        if (addressee != null) {
            Consumer<String> responder = users.get(author).respond();
            var messages = messagesRepository.lastNMessages(author, addressee, quantity);
            if (messages.isEmpty()) return;
            var messagesText = messages.stream()
                    .map(Message::toString)
                    .collect(Collectors.joining(System.lineSeparator()));
            responder.accept("Server:" + System.lineSeparator() + messagesText);
        }
    }

    public synchronized void syncUserChat(User author) {
        var addressee = chats.get(author);
        if (addressee != null) {
            Consumer<String> responder = users.get(author).respond();
            var messages = messagesRepository.lastMessages(author, addressee);
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
            users.get(addressee).respond().accept(message.toString());
        }
        message.markAsRead(author);
        users.get(author).respond().accept(message.toString());

        messagesRepository.add(message);
    }

    public synchronized User currentTalkerOf(User user) {
        return chats.get(user);
    }

    public synchronized List<User> whoIsOnline() {
        return users.keySet().stream()
                .sorted(Comparator.comparing(User::toString))
                .toList();
    }

    public synchronized void removeUser(User user) {
        users.get(user).logOut();
        chats.remove(user);
        users.remove(user);
    }


    public synchronized void makeChat(User currentUser, User secondUser) {
        if (registrationRepository.isRegistered(secondUser.toString())) {
            chats.put(currentUser, secondUser);
        }
    }

    public void sendNotification(User user, String text) {
        users.get(user).respond().accept(text);
    }

    public List<User> findUnreadUsers(User user) {
        return messagesRepository.findWhoSendNewMessages(user);
    }

    public String statistics(User user) {
        var addressee = chats.get(user);
        return addressee != null ? messagesRepository.statsOfChat(user, addressee) : "";
    }
}
