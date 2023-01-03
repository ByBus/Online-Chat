package chat.server.authentication;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class StringToAuthMapTest {

    @Test
    public void testMapToString1() {
        var delimiter = "-||-";
        var mapper = new StringToAuthMap();
        var map = Map.of(
                "user1", "1234"
        );
        var expected = "user1-||-1234";
        var actual = mapper.mapFrom(map);
        assertEquals(expected, actual);
    }

    @Test
    public void testMapToString2() {
        var delimiter = "-||-";
        var mapper = new StringToAuthMap();
        var map = Map.of(
                "user1", "1234",
                "user2", "qwerty"
        );
        var expected = "user1-||-1234\nuser2-||-qwerty";
        var actual = mapper.mapFrom(map);
        assertEquals(expected, actual);
    }


    @Test
    public void testStringToMap1() {
        var mapper = new StringToAuthMap();
        var n = System.lineSeparator();
        var input = "user1-||-1234"+n+"user2-||-qwerty";
        var expected = Map.of(
                "user1", "1234",
                "user2", "qwerty"
        );
        var actual = mapper.map(input);
        assertEquals(expected, actual);
    }
}