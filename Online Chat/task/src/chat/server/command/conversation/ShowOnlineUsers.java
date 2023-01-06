package chat.server.command.conversation;

import chat.server.MessageDispatcher;
import chat.server.model.User;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ShowOnlineUsers extends ConversationCommand {
    private final MessageDispatcher messageDispatcher;

    public ShowOnlineUsers(MessageDispatcher messageDispatcher) {
        super("/list", 0);
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    protected String execute(String[] params, User user) {
        var users = new ArrayList<>(messageDispatcher.whoIsOnline());
        users.remove(user);
        var online = "online: " + users.stream().map(User::toString).collect(Collectors.joining(" "));
        return "Server: " + (users.isEmpty() ? "no one online" : online);
    }
}
