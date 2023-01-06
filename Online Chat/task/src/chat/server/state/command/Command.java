package chat.server.state.command;

import chat.server.exception.IncorrectCommandException;
import chat.server.exception.RespondException;
import chat.server.model.User;

public abstract class Command {
    private final String pattern;
    Command next;
    private final int parametersNumber;

    Command(String pattern, int parametersNumber) {
        this.pattern = pattern;
        this.parametersNumber = parametersNumber + 1;
    }

    public String handle(String input, User user) throws RespondException {
        if (checkPattern(input)){
            var params = parameters(input);
            if (checkIncorrectParamsNumber(params)) throw new IncorrectCommandException();
            return execute(params, user);
        } else if (next != null) {
            return next.handle(input, user);
        }
        return "";
    }

    protected boolean checkPattern(String input) {
        return input.startsWith(pattern);
    }

    protected boolean checkIncorrectParamsNumber(String[] params) {
        return params.length != parametersNumber;
    }

    abstract String execute(String[] params, User user) throws RespondException;

    public Command setNext(Command next) {
        this.next = next;
        return next;
    }

    String[] parameters(String command) {
        return command.trim().split("\\s+");
    }
}
