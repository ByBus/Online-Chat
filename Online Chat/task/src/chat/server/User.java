package chat.server;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class User implements Observer<Message> {
    private String name;
    private final Consumer<List<Message>> messagesUpdater;

    public User(String name, Consumer<List<Message>> messagesUpdater) {
        this.name = name;
        this.messagesUpdater = messagesUpdater;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (other == null || getClass() != other.getClass()) return false;
        User user = (User) other;
        return Objects.equals(name, user.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public void update(List<Message> data) {
        messagesUpdater.accept(data);
    }
}
