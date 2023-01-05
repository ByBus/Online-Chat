package chat.server.exception;

public class NotModeratorException extends RespondException {
    public NotModeratorException() {
        super("Server: you are not a moderator or an admin!");
    }
}
