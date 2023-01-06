package chat.server.state;

import chat.server.exception.RespondException;

public interface Command<T> {
    String handle(String input, T additionalParameter) throws RespondException;

    Command<T> setNext(Command<T> next);
}
