package chat.server.command.conversation;

import chat.server.communication.MessageDispatcher;
import chat.server.model.User;

public class ShowChatStatistics extends ConversationCommand {
    private final MessageDispatcher messageDispatcher;

    public ShowChatStatistics(MessageDispatcher messageDispatcher) {
        super("/stats", 0);
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    public String execute(String[] params, User user) {
        return messageDispatcher.statistics(user);
    }
}
