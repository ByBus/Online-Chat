package chat.server.servicelocator;

import chat.server.*;
import chat.server.authentication.*;
import chat.server.command.AuthAndRegistration;
import chat.server.command.Command;
import chat.server.command.Conversation;
import chat.server.data.FileReader;
import chat.server.data.PasswordRepository;
import chat.server.data.Serializer;
import chat.server.data.MessagesRepository;
import chat.server.model.Message;
import chat.server.model.User;

import java.util.List;

public class ServiceLocator {
    private static MessageDispatcher messageDispatcher;
    private static MessagesRepository messagesRepository;
    private static PasswordRepository passwordRepository;

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
            messageDispatcher = new MessageDispatcher(provideMessagesRepository(), providePasswordRepository());
        }
        return messageDispatcher;
    }

    public static StringToAuthMap provideMapper() {
        return new StringToAuthMap();
    }

    public static FileReader provideUsersFileReader() {
        return new FileReader("usersdb.txt");
    }

    public static PasswordRepository providePasswordRepository() {
        if (passwordRepository == null) {
            passwordRepository = new PasswordRepository(provideUsersFileReader(), provideMapper());
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
                providePasswordRepository(),
                provideMessageDispatcher(),
                provideStringHasher(),
                providePasswordChecker()
        );
    }

    public synchronized static Command provideAuthAndRegisterState(Session session) {
        return new AuthAndRegistration(provideAuthenticator(), session);
    }

    public synchronized static Command provideConversationState(User user) {
        return new Conversation(provideMessageDispatcher(), user);
    }
}
