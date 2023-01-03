package chat.server.authentication;

public interface Mapper<T, R> {
    R map(T value);

    T mapFrom(R value);
}
