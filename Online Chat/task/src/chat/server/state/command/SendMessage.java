package chat.server.state.command;

import chat.server.MessageDispatcher;
import chat.server.exception.RespondException;
import chat.server.model.Message;
import chat.server.model.User;

public class SendMessage extends Command{
    private final MessageDispatcher messageDispatcher;

    public SendMessage(MessageDispatcher messageDispatcher) {
        super("", 0);
        this.messageDispatcher = messageDispatcher;
    }

    @Override
    String execute(String[] params, User user) throws RespondException {
        User secondTalker = messageDispatcher.currentTalkerOf(user);
        if (secondTalker != null) {
            var message = new Message(user, secondTalker, String.join(" ", params));
            messageDispatcher.addMessage(message);
        } else {
            return "Server: use /list command to choose a user to text!";
        }
        return "";
    }

    @Override
    protected boolean checkPattern(String input) {
        return true;
    }

    @Override
    protected boolean checkIncorrectParamsNumber(String[] params) {
        return false;
    }
}
