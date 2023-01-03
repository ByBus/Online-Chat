package chat.server.authentication;

public interface Checker<T> {
    boolean check(T value);
}
