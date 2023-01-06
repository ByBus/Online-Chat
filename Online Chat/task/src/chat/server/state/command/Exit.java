package chat.server.state.command;

import chat.server.MessageDispatcher;
import chat.server.exception.ExitException;
import chat.server.model.User;

public class Exit extends Command {
    private final MessageDispatcher messageDispatcher;

    public Exit(MessageDispatcher messageDispatcher) {
        super("/exit", 0);
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    String execute(String[] params, User user) {
        messageDispatcher.removeUser(user);
        throw new ExitException();
    }
}
