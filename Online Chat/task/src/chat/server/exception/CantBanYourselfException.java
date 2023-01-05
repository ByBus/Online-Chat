package chat.server.exception;

public class CantBanYourselfException extends RespondException {
    public CantBanYourselfException() {
        super("Server: you can't kick yourself!");
    }
}
