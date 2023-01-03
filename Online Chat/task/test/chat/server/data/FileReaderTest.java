package chat.server.data;

import org.junit.After;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

public class FileReaderTest {
    private final String filename = "fileReader_test_file.txt";
    private final String newLineSymbol = System.lineSeparator();

    @After
    public void removeFile() {
        new File(filename).delete();
    }

    @Test
    public void test_no_write_read() {
        var fileReader = new FileReader(filename);
        String actual;
        try {
            actual = fileReader.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var expected = "";
        assertEquals(expected, actual);
    }


    @Test
    public void test_write1_read() {
        var fileReader = new FileReader(filename);
        String actual;
        try {
            fileReader.save("qwerty");
            actual = fileReader.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var expected = "qwerty".formatted(newLineSymbol);
        assertEquals(expected, actual);
    }

    @Test
    public void test_write2_read() {
        var fileReader = new FileReader(filename);
        String actual;
        try {
            fileReader.save("text");
            fileReader.save("text2");
            actual = fileReader.read();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var expected = "text%stext2".formatted(newLineSymbol);
        assertEquals(expected, actual);
    }
}