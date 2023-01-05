package chat.server.exception;

public class AlreadyModeratorException extends RespondException{
    public AlreadyModeratorException() {
        super("Server: this user is already a moderator!");
    }
}
