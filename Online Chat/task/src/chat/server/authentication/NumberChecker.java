package chat.server.authentication;

public class NumberChecker implements Checker<String> {
    @Override
    public boolean check(String value) {
        if (value == null) {
            return false;
        }
        try {
            Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}
