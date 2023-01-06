package chat.server.command.registration;

import chat.server.Communication;
import chat.server.exception.ExitException;
import chat.server.exception.RespondException;

public class ExitFromRegistration extends RegistrationCommand{
    public ExitFromRegistration() {
        super("/exit", 0);
    }

    @Override
    protected String execute(String[] params, Communication session) throws RespondException {
        throw new ExitException();
    }
}
