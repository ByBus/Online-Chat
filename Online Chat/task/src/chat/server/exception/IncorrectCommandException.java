package chat.server.exception;

public class IncorrectCommandException extends RespondException {
    public IncorrectCommandException() {
        super("Server: incorrect command!");
    }
}
