package chat.server.exception;

public class IncorrectLoginException extends RespondException {
    public IncorrectLoginException() {
        super("Server: incorrect login!");
    }
}