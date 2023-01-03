package chat.server.authentication;

import chat.server.MessageDispatcher;
import chat.server.Session;
import chat.server.exception.*;
import chat.server.model.User;
import chat.server.data.PasswordRepository;

import java.io.IOException;

public class Authenticator {
    private final Hasher<String> passwordHasher;
    private final Checker<String> passwordChecker;
    private final PasswordRepository repository;
    private final MessageDispatcher messageDispatcher;

    public Authenticator(PasswordRepository repository,
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
        if(repository.isRegistered(name)) throw new LoginAlreadyTaken();
        repository.register(name, hash);
        return true;
    }

    public void login(String username, Session session) {
        User user = new User(username);
        session.setUser(user);
        messageDispatcher.addUser(user, session.respond());
    }
}
