package chat.server.state.command;

import chat.server.MessageDispatcher;
import chat.server.model.User;

public class CreateChatWithUser extends Command{
    private final MessageDispatcher messageDispatcher;

    public CreateChatWithUser(MessageDispatcher messageDispatcher) {
        super("/chat", 1);
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    String execute(String[] params, User user) {
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
