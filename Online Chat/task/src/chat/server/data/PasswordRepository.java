package chat.server.data;

import chat.server.authentication.Mapper;

import java.io.IOException;
import java.util.Map;

public class PasswordRepository {
    private final FileReader storage;
    private final Mapper<String, Map<String, String>> mapper;

    public PasswordRepository(FileReader storage, Mapper<String, Map<String, String>> mapper) {
        this.storage = storage;
        this.mapper = mapper;
    }

    public void register(String name, String passwordHash) throws IOException {
        storage.save(mapper.mapFrom(Map.of(name, passwordHash)));
    }

    public boolean checkRegistration(String name, String passwordHash) throws IOException {
        var registeredUsers = storage.read();
        var namesToPasswords = mapper.map(registeredUsers);
        return !namesToPasswords
                .entrySet()
                .stream()
                .filter(e -> e.getKey().equals(name) && e.getValue().equals(passwordHash))
                .toList()
                .isEmpty();
    }

    public boolean isRegistered(String name) throws IOException {
        var registeredUsers = storage.read();
        if (registeredUsers.isBlank()) return false;
        var namesToPasswords = mapper.map(registeredUsers);
        return namesToPasswords.containsKey(name);
    }
}
