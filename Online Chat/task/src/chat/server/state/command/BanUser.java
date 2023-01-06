package chat.server.state.command;

import chat.server.authentication.Authorizator;
import chat.server.exception.RespondException;
import chat.server.model.User;

public class BanUser extends Command{
    private final Authorizator authorizator;

    public BanUser(Authorizator authorizator) {
        super("/kick ", 1);
        this.authorizator = authorizator;
    }

    @Override
    String execute(String[] params, User user) throws RespondException {
        var banUser = new User(params[1]);
        authorizator.ban(user, banUser);
        return "Server: " + banUser + " was kicked!";
    }
}
