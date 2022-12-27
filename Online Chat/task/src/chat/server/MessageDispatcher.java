package chat.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MessageDispatcher implements Observable<Message> {
    private final Set<User> users = new HashSet<>();
    private final List<Message> messages = new ArrayList<>();

    synchronized Boolean addUser(User user) {
        boolean added = users.add(user);
        if (added) {
            user.update(lastTenMessages());
        }
        return added;
    }

    synchronized void addMessage(Message message) {
        messages.add(message);
        notifyObservers(List.of(message));
    }

    synchronized void removeUser(User user) {
        users.remove(user);
    }

    private List<Message> lastTenMessages() {
        return (messages.size() >= 10)
                ? messages.subList(messages.size() - 10, messages.size())
                : messages.stream().toList();
    }


    @Override
    public void notifyObservers(List<Message> data) {
        if (!users.isEmpty() && !data.isEmpty()) {
            users.forEach(user -> user.update(data));
        }
    }
}
