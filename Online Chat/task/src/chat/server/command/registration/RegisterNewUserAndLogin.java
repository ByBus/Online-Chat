package chat.server.command.registration;

import chat.server.communication.Communication;
import chat.server.authentication.Authenticator;
import chat.server.exception.RespondException;

public class RegisterNewUserAndLogin extends RegistrationCommand{
    private final Authenticator authenticator;

    public RegisterNewUserAndLogin(Authenticator authenticator) {
        super("/registration ", 2);
        this.authenticator = authenticator;
    }

    @Override
    protected String execute(String[] params, Communication session) throws RespondException {
        String username = params[1];
        String password = params[2];
        if (authenticator.register(username, password)) {
            authenticator.login(username, session);
        }
        return "Server: you are registered successfully!";
    }
}
