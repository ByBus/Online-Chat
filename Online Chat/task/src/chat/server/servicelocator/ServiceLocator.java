package chat.server.servicelocator;

import chat.server.*;
import chat.server.authentication.*;
import chat.server.command.conversation.*;
import chat.server.command.registration.ExitFromRegistration;
import chat.server.command.registration.LoginUser;
import chat.server.command.registration.RegisterNewUserAndLogin;
import chat.server.state.AuthAndRegistration;
import chat.server.state.Command;
import chat.server.state.State;
import chat.server.state.Conversation;
import chat.server.data.*;
import chat.server.model.Message;
import chat.server.model.Registration;
import chat.server.model.User;

import java.util.List;

public class ServiceLocator {
    private static MessageDispatcher messageDispatcher;
    private static MessagesRepository messagesRepository;
    private static RegistrationRepository passwordRepository;

    public static Serializer<List<Message>> provideMessageSerializer() {
        return new Serializer<>("messagedb.txt");
    }

    public static MessagesRepository provideMessagesRepository() {
        if (messagesRepository == null) {
            messagesRepository = new MessagesRepository(provideMessageSerializer());
        }
        return messagesRepository;
    }

    public static MessageDispatcher provideMessageDispatcher() {
        if (messageDispatcher == null) {
            messageDispatcher = new MessageDispatcher(provideMessagesRepository(), provideRegistrationRepository());
        }
        return messageDispatcher;
    }

    private static DataSource<Registration> provideLocalRegistrationDataSource() {
        return new LocalRegistrationDataSource(new Serializer<>("usersdb.txt"));
    }

    private static DataSource<Registration> provideInMemoryRegistrationDataSource() {
        return new InMemoryRegistrationDataSource();
    }

    private static Registration provideAdmin() {
        var registration = new Registration("admin", provideStringHasher().hash("12345678"));
        registration.makeAdmin();
        return registration;
    }

    public static RegistrationRepository provideRegistrationRepository() {
        if (passwordRepository == null) {
            passwordRepository = new RegistrationRepository(
                    provideInMemoryRegistrationDataSource(),
                    provideLocalRegistrationDataSource()
            );
            passwordRepository.register(provideAdmin());
        }
        return passwordRepository;
    }

    public static Checker<String> providePasswordChecker() {
        return new PasswordChecker();
    }

    public static Hasher<String> provideStringHasher() {
        return new PasswordHasher();
    }

    public static Authenticator provideAuthenticator() {
        return new Authenticator(
                provideRegistrationRepository(),
                provideMessageDispatcher(),
                provideStringHasher(),
                providePasswordChecker()
        );
    }

    public static Authorizator provideAuthorizator() {
        return new Authorizator(provideMessageDispatcher(), provideRegistrationRepository());
    }

    public synchronized static State provideAuthAndRegisterState(Communication session) {
        return new AuthAndRegistration(provideRegistrationCommands(), session);
    }

    public synchronized static State provideConversationState(User user) {
        return new Conversation(provideChainOfCommands(), user);
    }

    public static Command<User> provideChainOfCommands() {
        MessageDispatcher dispatcher = provideMessageDispatcher();
        Authorizator authorizator = provideAuthorizator();
        Command<User> start = new ShowOnlineUsers(dispatcher);
        start
                .setNext(new CreateChatWithUser(dispatcher))
                .setNext(new ShowUnreadMessages(dispatcher))
                .setNext(new ShowChatStatistics(dispatcher))
                .setNext(new ShowMessagesHistory(dispatcher))
                .setNext(new Exit(dispatcher))
                .setNext(new BanUser(authorizator))
                .setNext(new MakeUserAModerator(authorizator))
                .setNext(new MakeModeratorAUser(authorizator))
                .setNext(new SendMessage(dispatcher));

        return start;
    }

    public static Command<Communication> provideRegistrationCommands() {
        Authenticator authenticator = provideAuthenticator();
        Command<Communication> first = new RegisterNewUserAndLogin(authenticator);
        first
                .setNext(new LoginUser(authenticator))
                .setNext(new ExitFromRegistration());
        return first;
    }
}
