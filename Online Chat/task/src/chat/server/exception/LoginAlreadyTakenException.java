package chat.server.exception;

public class LoginAlreadyTakenException extends RespondException{
    public LoginAlreadyTakenException() {
        super("Server: this login is already taken! Choose another one.");
    }
}
