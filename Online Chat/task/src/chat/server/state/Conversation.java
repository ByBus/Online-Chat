package chat.server.state;

import chat.server.exception.RespondException;
import chat.server.model.User;
import chat.server.state.command.Command;

public class Conversation implements State {
    private final Command chainOfCommands;
    private final User user;

    public Conversation( Command chainOfCommands, User currentUser) {
        this.chainOfCommands = chainOfCommands;
        this.user = currentUser;
    }

    @Override
    public String execute(String command) throws RespondException {
        return chainOfCommands.handle(command, user);
    }
}
