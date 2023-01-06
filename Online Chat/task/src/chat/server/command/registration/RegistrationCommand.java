package chat.server.command.registration;

import chat.server.Communication;
import chat.server.state.Command;
import chat.server.command.CommandInputChecks;
import chat.server.exception.IncorrectCommandException;
import chat.server.exception.NotInChatException;
import chat.server.exception.RespondException;

public abstract class RegistrationCommand extends CommandInputChecks implements Command<Communication>  {

    private Command<Communication> next;

    protected RegistrationCommand(String pattern, int parametersNumber) {
        super(pattern, parametersNumber);
    }

    @Override
    public String handle(String input, Communication additionalParameter) throws RespondException {
        String response;
        var params = parameters(input);

        if (checkPattern(input)){
            if (checkIncorrectParamsNumber(params)) throw new IncorrectCommandException();
            response = execute(params, additionalParameter);
        } else if (next != null) {
            response = next.handle(input, additionalParameter);
        } else {
            throw new NotInChatException();
        }
        return response;
    }

    @Override
    public Command<Communication> setNext(Command<Communication> next) {
        this.next = next;
        return next;
    }

    protected abstract String execute(String[] params, Communication session) throws RespondException;
}
