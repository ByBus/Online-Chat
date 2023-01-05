package chat.server.data;

import java.util.List;

public interface DataSource<T> {
    void save(T item);
    void saveAll(List<T> items);
    List<T> readAll();
}
