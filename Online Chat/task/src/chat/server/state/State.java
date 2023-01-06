package chat.server.state;

import chat.server.exception.RespondException;

import java.io.IOException;

public interface State {
    String execute(String command) throws RespondException, IOException;


}
