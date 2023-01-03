package chat.server.data;

import org.junit.After;
import org.junit.Test;

import java.io.*;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class SerializerTest {
    private final String filename = "serializer_test_file.txt";

    @After
    public void removeFile() {
        new File(filename).delete();
    }

    @Test
    public void test_save_list_string_and_read() {
        var serializer = new Serializer<List<String>>(filename);
        List<String> actual;
        try {
            serializer.serialize(List.of("123", "George", ""));
            actual = serializer.deserialize();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        List<String> expected = List.of("123", "George", "");
        assertEquals(expected, actual);
    }

    @Test
    public void test_save_list_int_and_read() {
        var serializer = new Serializer<List<Integer>>(filename);
        List<Integer> actual;
        try {
            serializer.serialize(List.of(2, 5, 178));
            actual = serializer.deserialize();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        List<Integer> expected = List.of(2, 5, 178);
        assertEquals(expected, actual);
    }

    @Test
    public void test_save_int_and_read() {
        var serializer = new Serializer<Integer>(filename);
        int actual;
        try {
            serializer.serialize(1024);
            actual = serializer.deserialize();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        int expected = 1024;
        assertEquals(expected, actual);
    }

    @Test(expected = FileNotFoundException.class)
    public void test_read_not_existing_file() throws IOException, ClassNotFoundException {
        var serializer = new Serializer<List<String>>(filename);
        serializer.deserialize();
    }

    @Test(expected = EOFException.class)
    public void test_read_empty_file() throws IOException, ClassNotFoundException {
        var serializer = new Serializer<List<String>>(filename);
        new File(filename).createNewFile();
        serializer.deserialize();
    }

    @Test(expected = ClassCastException.class)
    public void test_read_incorrect_file() throws IOException, ClassNotFoundException {
        var serializerRead = new Serializer<List<String>>(filename);
        var serializerWrite = new Serializer<String>(filename);
        String text = "ssadasfsdvuntyim dtrrt";
        serializerWrite.serialize(text);

        var actual = serializerRead.deserialize();
        System.out.println(actual);
    }

    @Test(expected = StreamCorruptedException.class)
    public void test_read_not_serialized_file() throws IOException, ClassNotFoundException {
        try(DataOutputStream outstream= new DataOutputStream(new FileOutputStream(filename,false))) {
            String body = "kjdsvhaAHJhjvhdfj1 gjfdkjhbbsn ";
            outstream.write(body.getBytes());
        } catch (IOException ignore) {
        }
        var serializer = new Serializer<List<String>>(filename);
        var actual = serializer.deserialize();
    }
}