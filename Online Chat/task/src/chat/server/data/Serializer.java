package chat.server.data;

import java.io.*;

public class Serializer<T> {
    private final String fileName;

    public Serializer(String fileName) {
        this.fileName = fileName;
    }

    public synchronized void serialize(T obj) throws IOException {
        try(FileOutputStream fos = new FileOutputStream(fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(obj);
        }
    }

    public synchronized T deserialize() throws IOException, ClassNotFoundException {
        try (FileInputStream fis = new FileInputStream(fileName);
             BufferedInputStream bis = new BufferedInputStream(fis);
             ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (T) ois.readObject();
        }
    }
}
