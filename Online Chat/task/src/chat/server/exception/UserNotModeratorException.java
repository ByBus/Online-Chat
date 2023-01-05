package chat.server.exception;

public class UserNotModeratorException extends RespondException{
    public UserNotModeratorException() {
        super("Server: this user is not a moderator!");
    }
}