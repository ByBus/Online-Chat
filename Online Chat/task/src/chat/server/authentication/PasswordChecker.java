package chat.server.authentication;

public class PasswordChecker implements Checker<String> {
    @Override
    public boolean check(String value) {
        return value.length() >= 8;
    }
}
