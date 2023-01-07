package chat.server.command.conversation;

import chat.server.communication.MessageDispatcher;
import chat.server.exception.ExitException;
import chat.server.model.User;

public class Exit extends ConversationCommand {
    private final MessageDispatcher messageDispatcher;

    public Exit(MessageDispatcher messageDispatcher) {
        super("/exit", 0);
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    protected String execute(String[] params, User user) {
        messageDispatcher.removeUser(user);
        throw new ExitException();
    }
}
