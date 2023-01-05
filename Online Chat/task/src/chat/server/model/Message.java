package chat.server.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Message implements Serializable {
    @Serial
    private static final long serialVersionUID = 9878654321L;
    private final long time;
    private final Map<User, Boolean> whoIsRead = new HashMap<>();
    private final User author;
    private final User addressee;
    private final String text;

    public Message(User from, User to, String text) {
        this.author = from;
        this.addressee = to;
        this.whoIsRead.put(author, false);
        this.whoIsRead.put(addressee, false);
        this.text = text;
        this.time = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return author + ": " + text;
    }

    public void markAsRead(User user) {
        whoIsRead.computeIfPresent(user, (k, v) -> true);
    }

    public User addressee() {
        return addressee;
    }

    public User author() {
        return author;
    }

    public String prepareForUser(User user) {
        var newMark = user.equals(addressee) && !whoIsRead.get(user) ? "(new) " : "";
        return newMark + this;
    }
    public boolean isChatting(User user) {
        return author.equals(user) || addressee.equals(user);
    }

    public boolean isReadBy(User user){
        if (!whoIsRead.containsKey(user)) throw new IllegalArgumentException();
        return whoIsRead.get(user);
    }

    public boolean isChatting(User... user) {
       return Arrays.stream(user).allMatch(whoIsRead::containsKey);
    }
}
