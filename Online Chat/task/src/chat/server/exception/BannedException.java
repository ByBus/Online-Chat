package chat.server.exception;

public class BannedException extends RespondException {
    public BannedException() {
        super("Server: you are banned!");
    }
}
