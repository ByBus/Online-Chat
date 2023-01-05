package chat.server.model;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Registration implements Serializable {
    @Serial
    private static final long serialVersionUID = 2L;
    private static final int ADMIN = 2;
    private static final int MODERATOR = 1;
    private static final int USER = 0;
    private final String name;
    private final String passwordHash;
    private int role = USER;
    private long banExpirationTime = 0L;

    public Registration(String name, String passwordHash) {
        this.name = name;
        this.passwordHash = passwordHash;
    }

    public String getName() {
        return name;
    }

    public boolean isAdmin() {
        return role == ADMIN;
    }

    public void makeAdmin() {
        role = ADMIN;
    }

    public boolean checkCredentials(String name, String passwordHash) {
        return this.name.equals(name) && this.passwordHash.equals(passwordHash);
    }

    public void ban(boolean state) {
        banExpirationTime = state ? System.currentTimeMillis() + 300_000 : 0; //for 5 minutes
    }

    public boolean isBanned() {
        return banExpirationTime > System.currentTimeMillis();
    }

    public boolean isModerator() {
        return role == MODERATOR;
    }

    public void setModerator(boolean state) {
        role = state ? MODERATOR : USER;
    }

    public boolean hasMoreAuthorityThan(Registration other) {
        return this.role > other.role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Registration that = (Registration) o;
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
