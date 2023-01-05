package chat.server.authentication;

import chat.server.Communication;
import chat.server.MessageDispatcher;
import chat.server.data.RegistrationRepository;
import chat.server.exception.*;
import chat.server.model.User;

import java.io.IOException;

public class Authenticator {
    private final Hasher<String> passwordHasher;
    private final Checker<String> passwordChecker;
    private final RegistrationRepository repository;
    private final MessageDispatcher messageDispatcher;

    public Authenticator(RegistrationRepository repository,
                         MessageDispatcher messageDispatcher,
                         Hasher<String> passwordHasher,
                         Checker<String> passwordChecker) {
        this.passwordHasher = passwordHasher;
        this.passwordChecker = passwordChecker;
        this.repository = repository;
        this.messageDispatcher = messageDispatcher;
    }

    public boolean checkLogin(String name, String password) throws RespondException, IOException {
        if (!repository.isRegistered(name)) throw new IncorrectLoginException();
        var hash = passwordHasher.hash(password);
        if (!repository.checkRegistration(name, hash)) throw new IncorrectPasswordException();
        return true;
    }

    public boolean register(String name, String password) throws RespondException, IOException {
        if (!passwordChecker.check(password)) throw new ShortPasswordException();
        var hash = passwordHasher.hash(password);
        if(repository.isRegistered(name)) throw new LoginAlreadyTakenException();
        repository.register(name, hash);
        return true;
    }

    public void login(String username, Communication session) throws RespondException {
        if(repository.isBanned(username)) throw new BannedException();
        User user = new User(username);
        session.setUser(user);
        messageDispatcher.addUser(user, session);
    }
}
