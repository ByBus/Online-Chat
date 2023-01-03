package chat.server.command;

import chat.server.MessageDispatcher;
import chat.server.exception.ExitException;
import chat.server.exception.IncorrectCommandException;
import chat.server.exception.RespondException;
import chat.server.model.Message;
import chat.server.model.User;

import java.util.stream.Collectors;

public class Conversation implements Command{
    private final MessageDispatcher messageDispatcher;
    private final User user;
    public Conversation(MessageDispatcher messageDispatcher, User currentUser) {
        this.messageDispatcher = messageDispatcher;
        this.user = currentUser;
    }
    @Override
    public String execute(String command) throws RespondException {
        var params = parameters(command);
        if (command.startsWith("/list")) {
            var users = messageDispatcher.whoIsOnline();
            users.remove(user);
            var online = "online: " + users.stream().map(User::toString).collect(Collectors.joining(" "));
            return "Server: " + (users.isEmpty() ? "no one online" : online);
        }
        if (command.startsWith("/chat ")) {
            if (params.length != 2) throw new IncorrectCommandException();
            var secondTalker = new User(params[1]);
            var response = "";
            if(!messageDispatcher.isOnline(secondTalker)) {
                response = "Server: the user is not online!";
            }
            messageDispatcher.makeChat(user, secondTalker);
            messageDispatcher.syncUserChat(user);
            return response;
        }
        if (command.startsWith("/exit")) {
            if (params.length != 1) throw new IncorrectCommandException();
            messageDispatcher.removeUser(user);
            throw new ExitException();
        }
        User secondTalker = messageDispatcher.currentTalkerOf(user);
        if (secondTalker != null) {
            var message = new Message(user, secondTalker, command.trim());
            messageDispatcher.addMessage(message);
        } else {
            return "Server: use /list command to choose a user to text!";
        }
        return "";
    }
    private String[] parameters(String command) {
        return command.trim().split("\\s+");
    }
}
