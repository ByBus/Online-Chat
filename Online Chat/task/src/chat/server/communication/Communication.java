package chat.server.communication;

import chat.server.model.User;

import java.util.function.Consumer;

public interface Communication {
    Consumer<String> respond();
    void logOut();
    void setUser(User user);
}
