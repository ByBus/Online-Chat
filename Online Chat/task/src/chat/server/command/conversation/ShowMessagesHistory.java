package chat.server.command.conversation;

import chat.server.MessageDispatcher;
import chat.server.authentication.NumberChecker;
import chat.server.model.User;

public class ShowMessagesHistory extends ConversationCommand {
    private final MessageDispatcher messageDispatcher;

    public ShowMessagesHistory(MessageDispatcher messageDispatcher) {
        super("/history ", 1);
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    protected String execute(String[] params, User user) {
        var messagesCount = params[1];
        var response = "";
        if (new NumberChecker().check(messagesCount)) {
            messageDispatcher.lastMessages(user, Integer.parseInt(messagesCount));
        } else {
            response = "Server: "+ messagesCount + " is not a number!";
        }
        return response;
    }
}
