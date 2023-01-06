package chat.server.data;

import chat.server.model.Registration;

public class RegistrationRepository {
    private final DataSource<Registration> inMemoryStorage;
    private final DataSource<Registration> localStorage;

    public RegistrationRepository(DataSource<Registration> inMemoryData,
                                  DataSource<Registration> onDiskData) {
        this.inMemoryStorage = inMemoryData;
        this.localStorage = onDiskData;
        inMemoryStorage.saveAll(localStorage.readAll());
    }

    public void register(String name, String passwordHash) {
        register(new Registration(name, passwordHash));
    }

    public void register(Registration registration){
        inMemoryStorage.save(registration);
    }

    public void setModerator(String name, boolean makeModerator) {
        inMemoryStorage.readAll()
                .stream()
                .filter(r -> r.getName().equals(name))
                .forEach(r -> r.setModerator(makeModerator));
    }

    public boolean checkRegistration(String name, String passwordHash) {
        var registrations = inMemoryStorage.readAll();
        return registrations.stream().anyMatch(r -> r.checkCredentials(name, passwordHash));
    }

    public Registration find(String name) {
        return inMemoryStorage.readAll().stream()
                .filter(r -> r.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public boolean isRegistered(String name){
        var registrations = inMemoryStorage.readAll();
        return registrations.stream().anyMatch(r -> r.getName().equals(name));
    }

    public void saveToDisk() {
        localStorage.saveAll(inMemoryStorage.readAll());
    }

    public boolean isBanned(String username) {
        var registration = find(username);
        if (registration == null){
            return false;
        }
        return registration.isBanned();
    }
}
