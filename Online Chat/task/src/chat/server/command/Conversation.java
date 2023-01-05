package chat.server.command;

import chat.server.MessageDispatcher;
import chat.server.authentication.Authorizator;
import chat.server.authentication.NumberChecker;
import chat.server.exception.ExitException;
import chat.server.exception.IncorrectCommandException;
import chat.server.exception.RespondException;
import chat.server.model.Message;
import chat.server.model.User;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class Conversation implements Command {
    private final MessageDispatcher messageDispatcher;
    private final Authorizator authorizator;
    private final User user;

    public Conversation(MessageDispatcher messageDispatcher, Authorizator authorizator, User currentUser) {
        this.messageDispatcher = messageDispatcher;
        this.authorizator = authorizator;
        this.user = currentUser;
    }

    @Override
    public String execute(String command) throws RespondException {
        var params = parameters(command);
        if (command.startsWith("/list")) {
            var users = new ArrayList<>(messageDispatcher.whoIsOnline());
            users.remove(user);
            var online = "online: " + users.stream().map(User::toString).collect(Collectors.joining(" "));
            return "Server: " + (users.isEmpty() ? "no one online" : online);
        }
        if (command.startsWith("/chat ")) {
            if (params.length != 2) throw new IncorrectCommandException();
            var secondTalker = new User(params[1]);
            var response = "";
            if (!messageDispatcher.isOnline(secondTalker)) {
                response = "Server: the user is not online!";
            }
            messageDispatcher.makeChat(user, secondTalker);
            messageDispatcher.syncUserChat(user);
            return response;
        }
        if (command.startsWith("/unread")) {
            if (params.length != 1) throw new IncorrectCommandException();
            var users = messageDispatcher.findUnreadUsers(user);
            var unreadUsers = "unread from: " + users.stream()
                    .map(User::toString)
                    .collect(Collectors.joining(" "));
            return "Server: " + (users.isEmpty() ? "no one unread" : unreadUsers);
        }
        if (command.startsWith("/stats")) {
            if (params.length != 1) throw new IncorrectCommandException();
            return messageDispatcher.statistics(user);
        }
        if (command.startsWith("/history ")){
            if (params.length != 2) throw new IncorrectCommandException();
            var messagesCount = params[1];
            var response = "";
            if (new NumberChecker().check(messagesCount)) {
                messageDispatcher.lastMessages(user, Integer.parseInt(messagesCount));
            } else {
                response = "Server: "+ messagesCount + " is not a number!";
            }
            return response;
        }
        if (command.startsWith("/exit")) {
            if (params.length != 1) throw new IncorrectCommandException();
            messageDispatcher.removeUser(user);
            throw new ExitException();
        }
        if (command.startsWith("/kick ")) {
            if (params.length != 2) throw new IncorrectCommandException();
            var banUser = new User(params[1]);
            authorizator.ban(user, banUser);
            return "Server: " + banUser + " was kicked!";
        }
        if (command.startsWith("/grant ")) {
            if (params.length != 2) throw new IncorrectCommandException();
            var moderator = new User(params[1]);
            authorizator.makeModerator(user, moderator);
            return "Server: " + moderator + " is the new moderator!";
        }
        if (command.startsWith("/revoke")) {
            if (params.length != 2) throw new IncorrectCommandException();
            var notModerator = new User(params[1]);
            authorizator.revokeModerator(user, notModerator);
            return "Server: " + notModerator + " is no longer a moderator!";
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
