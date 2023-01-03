package chat.server.exception;

public class ShortPasswordException extends RespondException {
    public ShortPasswordException() {
        super("Server: the password is too short!");
    }
}
