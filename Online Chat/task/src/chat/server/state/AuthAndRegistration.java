package chat.server.state;

import chat.server.communication.Communication;
import chat.server.exception.RespondException;

import java.io.IOException;

public class AuthAndRegistration implements State {
    private final Command<Communication> chainOfCommands;
    private final Communication session;

    public AuthAndRegistration(Command<Communication> chainOfCommands, Communication session) {
        this.chainOfCommands = chainOfCommands;
        this.session = session;
    }

    @Override
    public String execute(String input) throws RespondException, IOException {
        return chainOfCommands.handle(input, session);
    }
}
