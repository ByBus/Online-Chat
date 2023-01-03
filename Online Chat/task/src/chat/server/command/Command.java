package chat.server.command;

import chat.server.exception.RespondException;

import java.io.IOException;

public interface Command {
    String execute(String command) throws RespondException, IOException;


}
