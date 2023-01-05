package chat.server.data;

import chat.server.model.Registration;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class LocalRegistrationDataSource implements DataSource<Registration>{
    private final Serializer<List<Registration>> serializer;

    public LocalRegistrationDataSource(Serializer<List<Registration>> serializer) {
        this.serializer = serializer;
    }

    @Override
    public void save(Registration item) {
        try {
            var users = readAll();
            users.add(item);
            serializer.serialize(users);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveAll(List<Registration> items) {
        try {
            serializer.serialize(items);
        } catch (IOException e) {
            System.out.println("RegistrationsDB file is not yet created");
            //e.printStackTrace();
        }
    }

    @Override
    public List<Registration> readAll() {
        try {
            return serializer.deserialize();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("RegistrationsDB file is not yet created");
            //e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
