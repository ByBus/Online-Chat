package chat.server.state.command;

import chat.server.MessageDispatcher;
import chat.server.model.User;

public class ShowChatStatistics extends Command{
    private final MessageDispatcher messageDispatcher;

    public ShowChatStatistics(MessageDispatcher messageDispatcher) {
        super("/stats", 0);
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    String execute(String[] params, User user) {
        return messageDispatcher.statistics(user);
    }
}
