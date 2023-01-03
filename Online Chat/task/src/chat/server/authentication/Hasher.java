package chat.server.authentication;

public interface Hasher<T>{
    String hash(T value);
}
