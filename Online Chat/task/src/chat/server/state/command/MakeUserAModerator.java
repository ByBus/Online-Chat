package chat.server.state.command;

import chat.server.authentication.Authorizator;
import chat.server.exception.IncorrectCommandException;
import chat.server.exception.RespondException;
import chat.server.model.User;

public class MakeUserAModerator extends Command{
    private final Authorizator authorizator;

    public MakeUserAModerator(Authorizator authorizator) {
        super("/grant ", 1);
        this.authorizator = authorizator;
    }

    @Override
    String execute(String[] params, User user) throws RespondException {
        if (params.length != 2) throw new IncorrectCommandException();
        var moderator = new User(params[1]);
        authorizator.makeModerator(user, moderator);
        return "Server: " + moderator + " is the new moderator!";
    }
}
