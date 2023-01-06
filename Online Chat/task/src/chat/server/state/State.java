package chat.server.state;

import chat.server.exception.RespondException;

import java.io.IOException;

public interface State {
    String execute(String input) throws RespondException, IOException;
}
