package chat.server.authentication;

import chat.server.communication.MessageDispatcher;
import chat.server.data.RegistrationRepository;
import chat.server.exception.*;
import chat.server.model.User;

public class Authorizator {
    private final MessageDispatcher messageDispatcher;
    private final RegistrationRepository registrationRepository;

    public Authorizator(MessageDispatcher messageDispatcher, RegistrationRepository registrationRepository) {
        this.messageDispatcher = messageDispatcher;
        this.registrationRepository = registrationRepository;
    }

    public synchronized void ban(User authority, User banUser) throws RespondException {
        if (authority.equals(banUser)) throw new CantBanYourselfException();
        var authorityReg = registrationRepository.find(authority.toString());
        var banUserRegistration = registrationRepository.find(banUser.toString());
        if (!authorityReg.hasMoreAuthorityThan(banUserRegistration)) throw new NotModeratorException();
       // if (!authorityReg.hasMoreAuthorityThan(banUserRegistration)) throw new NotModeratorException();

        banUserRegistration.ban(true);
        messageDispatcher.sendNotification(banUser, "Server: you have been kicked out of the server!");
        messageDispatcher.removeUser(banUser);
    }

    public synchronized void makeModerator(User authority, User moderator) throws RespondException {
        var moderatorRegistration = registrationRepository.find(moderator.toString());
        if (moderatorRegistration.isModerator()) throw new AlreadyModeratorException();
        var authorityReg = registrationRepository.find(authority.toString());
        if(!authorityReg.isAdmin()) throw new NotAdminException();

        moderatorRegistration.setModerator(true);
        messageDispatcher.sendNotification(moderator, "Server: you are the new moderator now!");
    }

    public synchronized void revokeModerator(User authority, User user) throws RespondException {
        var authorityReg = registrationRepository.find(authority.toString());
        if(!authorityReg.isAdmin()) throw new NotAdminException();
        var userRegistration = registrationRepository.find(user.toString());
        if (!userRegistration.isModerator()) throw new UserNotModeratorException();

        userRegistration.setModerator(false);
        messageDispatcher.sendNotification(user, "Server: you are no longer a moderator!");
    }
}
