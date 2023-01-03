package chat.server.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class FileReader {
    private final File file;

    public FileReader(String filename) {
        file = new File(filename);
        try {
            file.createNewFile();
        } catch (IOException e) {
           e.printStackTrace();
        }
    }

    public synchronized void save(String text) throws IOException {
        try (FileWriter writer = new FileWriter(file, true)) {
            writer.write(text + System.lineSeparator());
        }
    }

    public synchronized String read() throws IOException {
        return Files.readString(file.toPath()).trim();
    }
}
