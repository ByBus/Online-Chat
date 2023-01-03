package chat.server.authentication;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class StringToAuthMap implements Mapper<String, Map<String, String>>{
    private static final String DELIMITER = ";=";

    @Override
    public Map<String, String> map(String value) {
        return Arrays.stream(value.split(System.lineSeparator()))
                .map(s -> s.split(DELIMITER))
                .collect(Collectors.toMap(e -> e[0], e -> e[1]));

    }

    @Override
    public String mapFrom(Map<String, String> value) {
        return value.entrySet()
                .stream()
                .map(e -> e.getKey() + DELIMITER + e.getValue())
                .collect(Collectors.joining(System.lineSeparator()));
    }
}
