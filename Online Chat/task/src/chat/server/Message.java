package chat.server;

public class Message {
    private final long time;
    private final User user;
    private String text;

    public Message(User user, String text) {
        this.user = user;
        this.text = text;
        this.time = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return user + ": " + text;
    }
}
