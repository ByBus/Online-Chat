package chat.server.exception;

public class IncorrectPasswordException extends RespondException {
    public IncorrectPasswordException() {
        super("Server: incorrect password!");
    }
}