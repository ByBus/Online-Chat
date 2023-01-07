package chat.server.command.conversation;

import chat.server.state.Command;
import chat.server.command.CommandInputChecks;
import chat.server.exception.IncorrectCommandException;
import chat.server.exception.RespondException;
import chat.server.model.User;

public abstract class ConversationCommand extends CommandInputChecks implements Command<User> {
    private Command<User> next;

    protected ConversationCommand(String pattern, int parametersNumber) {
        super(pattern, parametersNumber);
    }

    @Override
    public String handle(String input, User additionalParameter) throws RespondException {
        String response = "";
        var params = parseParameters(input);

        if (checkPattern(input)){
            if (!checkParamsNumber(params)) throw new IncorrectCommandException();
            response = execute(params, additionalParameter);
        } else if (next != null) {
            response = next.handle(input, additionalParameter);
        }
        return response;
    }

    @Override
    public Command<User> setNext(Command<User> next) {
        this.next = next;
        return next;
    }

    protected abstract String execute(String[] params, User user) throws RespondException;
}
