package chat.server;

import java.io.IOException;
import java.util.List;

public interface Observer<T> {
    void update(List<T> data) throws IOException;
}
