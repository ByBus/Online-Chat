package chat.server.command.conversation;

import chat.server.MessageDispatcher;
import chat.server.model.User;

public class CreateChatWithUser extends ConversationCommand {
    private final MessageDispatcher messageDispatcher;

    public CreateChatWithUser(MessageDispatcher messageDispatcher) {
        super("/chat", 1);
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    protected String execute(String[] params, User user) {
        var secondTalker = new User(params[1]);
        String response = "";
        if (!messageDispatcher.isOnline(secondTalker)) {
            response = "Server: the user is not online!";
        }
        messageDispatcher.makeChat(user, secondTalker);
        messageDispatcher.syncUserChat(user);
        return response;
    }
}
