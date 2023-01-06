package chat.server.command.conversation;

import chat.server.MessageDispatcher;
import chat.server.model.User;

import java.util.stream.Collectors;

public class ShowUnreadMessages extends ConversationCommand {
    private final MessageDispatcher messageDispatcher;

    public ShowUnreadMessages(MessageDispatcher messageDispatcher) {
        super("/unread", 0);
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    protected String execute(String[] params, User user) {
        var users = messageDispatcher.findUnreadUsers(user);
        var unreadUsers = "unread from: " + users.stream()
                .map(User::toString)
                .collect(Collectors.joining(" "));
        return "Server: " + (users.isEmpty() ? "no one unread" : unreadUsers);
    }
}
