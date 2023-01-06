package chat.server.state.command;

import chat.server.authentication.Authorizator;
import chat.server.exception.RespondException;
import chat.server.model.User;

public class MakeModeratorAUser extends Command{
    private final Authorizator authorizator;

    public MakeModeratorAUser(Authorizator authorizator) {
        super("/revoke", 1);
        this.authorizator = authorizator;
    }

    @Override
    String execute(String[] params, User user) throws RespondException {
        var notModerator = new User(params[1]);
        authorizator.revokeModerator(user, notModerator);
        return "Server: " + notModerator + " is no longer a moderator!";
    }
}
