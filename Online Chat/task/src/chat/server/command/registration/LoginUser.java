package chat.server.command.registration;

import chat.server.Communication;
import chat.server.authentication.Authenticator;
import chat.server.state.Command;
import chat.server.exception.RespondException;

public class LoginUser extends RegistrationCommand implements Command<Communication> {
    private final Authenticator authenticator;

    public LoginUser(Authenticator authenticator) {
        super("/auth ", 2);
        this.authenticator = authenticator;
    }

    @Override
    protected String execute(String[] params, Communication session) throws RespondException {
        String username = params[1];
        String password = params[2];
        if (authenticator.checkLogin(username, password)) {
            authenticator.login(username, session);
        }
        return "Server: you are authorized successfully!";
    }
}
