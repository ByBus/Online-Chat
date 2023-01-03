package chat.server.exception;

public class NotInChatException extends RespondException{
    public NotInChatException() {
        super("Server: you are not in the chat!");
    }
}
