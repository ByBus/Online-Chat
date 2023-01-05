package chat.server.exception;

public class NotAdminException extends RespondException{
    public NotAdminException() {
        super("Server: you are not an admin!");
    }
}
