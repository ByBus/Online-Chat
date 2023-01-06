package chat.server.state;

import chat.server.exception.RespondException;
import chat.server.model.User;

public class Conversation implements State {
    private final Command<User> chainOfCommands;
    private final User user;

    public Conversation(Command<User> chainOfCommands, User currentUser) {
        this.chainOfCommands = chainOfCommands;
        this.user = currentUser;
    }

    @Override
    public String execute(String input) throws RespondException {
        return chainOfCommands.handle(input, user);
    }
}
