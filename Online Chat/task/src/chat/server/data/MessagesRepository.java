package chat.server.data;

import chat.server.model.Message;
import chat.server.model.User;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MessagesRepository {
    private final Serializer<List<Message>> serializer;
    private final List<Message> messages;

    public MessagesRepository(Serializer<List<Message>> serializer) {
        this.serializer = serializer;
        messages = new ArrayList<>(read());
    }

    public void saveToDisk() throws IOException {
        serializer.serialize(messages);
    }

    public void add(Message message) {
        messages.add(message);
    }

    public List<Message> lastNMessages(User from, User to, int n) {
        int maxElements = 25;
        List<Message> chatMessages = chatMessages(from, to);
        int offset = Math.max(chatMessages.size() - n, 0);
        return chatMessages.stream().skip(offset).limit(maxElements).toList();
    }

    private List<Message> chatMessages(User from, User to) {
        return messages.stream()
                .filter(m -> m.isChatting(from, to))
                .toList();
    }

    public List<Message> lastMessages(User from, User to) {
        var chatMessages = chatMessages(from, to).stream()
                .collect(Collectors.groupingBy(m -> m.isReadBy(from)));
        var readMessages = chatMessages.getOrDefault(true, Collections.emptyList());
        var unreadMessages = chatMessages.getOrDefault(false, Collections.emptyList());
        int readOffset = Math.max(readMessages.size() - 10, 0); // maximum 10 read messages
        int unreadOffset = Math.max(unreadMessages.size() - 25, 0); // maximum 25 unread messages

        return Stream.concat(
                readMessages.stream().skip(readOffset),
                unreadMessages.stream().skip(unreadOffset)
        ).toList();
    }

    public List<Message> read() {
        List<Message> messages = Collections.emptyList();
        try {
            messages = serializer.deserialize();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("MessagesDb file not yet created.");
            // e.printStackTrace();
        }
        return messages;
    }

    public String statsOfChat(User author, User addressee) {
        var newLine = System.lineSeparator();
        StringBuilder sb = new StringBuilder("Server:");

        Map<User, Long> userToMessageCount = messages.stream()
                .filter(m -> m.isChatting(author) && m.isChatting(addressee))
                .collect(Collectors.groupingBy(Message::author, Collectors.counting()));

        long fromAuthor = userToMessageCount.getOrDefault(author, 0L);
        long fromAddressee = userToMessageCount.getOrDefault(addressee, 0L);
        long total = fromAuthor + fromAddressee;
        sb.append(newLine)
                .append("Statistics with ").append(addressee).append(":").append(newLine)
                .append("Total messages: ").append(total).append(newLine)
                .append("Messages from ").append(author).append(": ").append(fromAuthor).append(newLine)
                .append("Messages from ").append(addressee).append(": ").append(fromAddressee);
        return sb.toString();
    }

    public List<User> findWhoSendNewMessages(User addressee) {
        return messages.stream()
                .filter(m -> m.addressee().equals(addressee) && !m.isReadBy(addressee))
                .map(Message::author)
                .distinct()
                .sorted(Comparator.comparing(User::toString))
                .toList();
    }
}
