package chat.server.exception;

public class LoginAlreadyTaken extends RespondException{
    public LoginAlreadyTaken() {
        super("Server: this login is already taken! Choose another one.");
    }
}
