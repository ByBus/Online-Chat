package chat.server.authentication;

public class PasswordHasher implements Hasher<String> {
    @Override
    public String hash(String value) {
        return String.valueOf(value.hashCode());
    }
}
