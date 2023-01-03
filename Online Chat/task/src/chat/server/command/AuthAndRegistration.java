package chat.server.command;

import chat.server.Session;
import chat.server.authentication.Authenticator;
import chat.server.exception.IncorrectCommandException;
import chat.server.exception.NotInChatException;
import chat.server.exception.RespondException;

import java.io.IOException;

public class AuthAndRegistration implements Command {
    private final Authenticator authenticator;
    private final Session session;

    public AuthAndRegistration(Authenticator authenticator,
                               Session session) {
        this.authenticator = authenticator;
        this.session = session;
    }

    public String execute(String command) throws RespondException, IOException {
        var params = parameters(command);
        if (command.startsWith("/registration ")) {
            if (params.length != 3) throw new IncorrectCommandException();
            String username = params[1];
            String password = params[2];
            if (authenticator.register(username, password)) {
                authenticator.login(username, session);
            }
            return "Server: you are registered successfully!";
        }

        if ((command.startsWith("/auth "))) {
            if (params.length != 3) throw new IncorrectCommandException();
            String username = params[1];
            String password = params[2];
            if (authenticator.checkLogin(username, password)) {
                authenticator.login(username, session);
            }
            return "Server: you are authorized successfully!";
        }
        throw new NotInChatException();
    }

    private String[] parameters(String command) {
        return command.trim().split("\\s+");
    }
}
