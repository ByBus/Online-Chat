package chat.server.data;

import chat.server.model.Registration;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InMemoryRegistrationDataSource implements DataSource<Registration> {
    private final Set<Registration> registrations = new HashSet<>();

    @Override
    public void save(Registration item) {
        registrations.add(item);
    }

    @Override
    public void saveAll(List<Registration> items) {
        registrations.addAll(items);
    }

    @Override
    public List<Registration> readAll() {
        return registrations.stream().toList();
    }
}
