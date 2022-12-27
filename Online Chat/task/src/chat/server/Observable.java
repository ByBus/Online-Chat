package chat.server;

import java.util.List;

public interface Observable<T> {
    void notifyObservers(List<T> data);
}
