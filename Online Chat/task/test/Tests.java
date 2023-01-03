import org.hyperskill.hstest.dynamic.input.DynamicTestingMethod;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.hyperskill.hstest.common.Utils.sleep;

public class Tests extends StageTest<String> {
    private final int executePause = 50;
    private static final String userDBFileName = "usersdb.txt";
    private static final String tempUserDBFileName = "tempusersdb.txt";
    private static final String messageDBFileName = "messagedb.txt";
    private static final String tempMessageDBFileName = "tempmessagedb.txt";

    @DynamicTestingMethod
    CheckResult test1_Stage5() {
        final TestedProgram server = new TestedProgram("chat.server");
        final TestedProgram client1 = new TestedProgram("chat.client");
        final TestedProgram client2 = new TestedProgram("chat.client");
        final TestedProgram client3 = new TestedProgram("chat.client");
        client1.setReturnOutputAfterExecution(false);
        client2.setReturnOutputAfterExecution(false);
        client3.setReturnOutputAfterExecution(false);

        server.startInBackground();
        sleep(executePause);
        client1.start();
        sleep(executePause);
        client2.start();
        sleep(executePause);
        client3.start();
        sleep(executePause);
        client3.getOutput();
        client2.getOutput();

        final String client1Start = client1.getOutput().trim();
        if (!client1Start.contains("Server: authorize or register")) {
            return CheckResult.wrong(
                    "Can't find \"Server: authorize or register\" message");
        }

        client1.execute("bla bla bla");
        sleep(executePause);
        final String client1Answer1 = client1.getOutput().trim();
        if (!client1Answer1.contains("Server: you are not in the chat!")) {
            return CheckResult.wrong(
                    "Can't find  \"Server: you are not in the chat!\" " +
                            "message after trying to send a message before /auth or /register commands");
        }

        client1.execute("/auth asdasd asdasd");
        sleep(executePause);
        final String client1Answer2 = client1.getOutput().trim();
        if (!client1Answer2.contains("Server: incorrect login!")) {
            return CheckResult.wrong(
                    "Can't find \"Server: incorrect login!\" " +
                            "message after inputting wrong login and password");
        }

        client2.execute("/registration first pass");
        sleep(executePause);
        final String client2Answer1 = client2.getOutput().trim();
        if (!client2Answer1.contains("Server: the password is too short!")) {
            return CheckResult.wrong(
                    "Can't find \"Server: the password is too short!\" " +
                            "message after trying to register with short password");
        }

        client2.execute("/registration first 12345678");
        sleep(executePause);
        final String client2Answer2 = client2.getOutput().trim();
        if (!client2Answer2.contains("Server: you are registered successfully!")) {
            return CheckResult.wrong(
                    "Can't find \"Server: you are registered successfully!\" " +
                            "message after successful authentication");
        }

        client2.execute("before choosing an addressee");
        sleep(executePause);
        final String client2Answer3 = client2.getOutput().trim();
        if (!client2Answer3.contains("Server: use /list command to choose a user to text!")) {
            return CheckResult.wrong(
                    "Can't find \"Server: use /list command to choose a user to text!\" " +
                            "message before choosing an addressee");
        }

        client2.execute("/list");
        sleep(executePause);
        final String client2Answer4 = client2.getOutput().trim();
        if (!client2Answer4.contains("Server: no one online")) {
            return CheckResult.wrong(
                    "Can't find \"Server: no one online\" message if there are no users online");
        }

        client2.execute("/exit");
        sleep(executePause);
        if (!client2.isFinished()) {
            return CheckResult.wrong("Client should be shut down, after the \"/exit\" command");
        }

        client1.execute("/auth first paasf");
        sleep(executePause);
        final String client1Answer3 = client1.getOutput().trim();
        if (!client1Answer3.contains("Server: incorrect password!")) {
            return CheckResult.wrong(
                    "Can't find \"Server: incorrect password!\" " +
                            "message after inputting a wrong password");
        }

        client1.execute("/auth first 12345678");
        sleep(executePause);
        final String client1Answer4 = client1.getOutput().trim();
        if (!client1Answer4.contains("Server: you are authorized successfully!")) {
            return CheckResult.wrong("Can't find \"Server: you are authorized " +
                    "successfully!\" message after successful authentication");
        }

        client3.execute("/registration first 12345678");
        sleep(executePause);
        final String client3Answer1 = client3.getOutput().trim();
        if (!client3Answer1.contains("Server: this login is already taken! Choose another one.")) {
            return CheckResult.wrong(
                    "Can't find \"Server: this login is already taken! Choose another one.\" " +
                            "message from a client that is trying to register with a login which is already taken");
        }

        client3.execute("/registration second 12345678");
        sleep(executePause);
        final String client3Answer2 = client3.getOutput().trim();
        if (!client3Answer2.contains("Server: you are registered successfully!")) {
            return CheckResult.wrong("Can't get the \"Server: you are registered " +
                    "successfully!\" message after successful authentication");
        }

        client1.execute("/list");
        sleep(executePause);
        final String client1Answer5 = client1.getOutput().trim();
        if (client1Answer5.contains("first")) {
            return CheckResult.wrong("The list of online users contains the client's name, but shouldn't");
        }

        if (!client1Answer5.contains("Server: online: second")) {
            return CheckResult.wrong("A client receive a wrong list of online users. " +
                    "Should be \"Server: online: second\"");
        }

        client1.execute("/chat blabla");
        sleep(executePause);
        final String client1Answer6 = client1.getOutput().trim();
        if (!client1Answer6.contains("Server: the user is not online!")) {
            return CheckResult.wrong("Can't find \"Server: the user is not online!\"" +
                    "after trying to chat using wrong username");
        }

        client1.execute("blabla");
        sleep(executePause);
        final String client1Answer7 = client1.getOutput().trim();
        if (!client1Answer7.contains("Server: use /list command to choose a user to text!")) {
            return CheckResult.wrong("Can't find \"Server: use /list command to " +
                    "choose a user to text!\" after trying to chat without choosing a user");
        }

        client1.execute("/chat second");
        sleep(executePause);

        client1.execute("test");
        sleep(executePause);
        final String client3Answer3 = client3.getOutput().trim();
        if (!client3Answer3.isEmpty()) {
            return CheckResult.wrong("Client \"second\" received a message \"" + client3Answer3 + "\" " +
                    "but shouldn't receive anything");
        }

        client3.execute("/chat first");
        sleep(executePause);
        final String client3Answer4 = client3.getOutput().trim();
        if (!client3Answer4.contains("(new) first: test")) {
            return CheckResult.wrong("Client \"second\" didn't receive " +
                    "a message in a format \"(new) userName: message\". " +
                    "Should be \"(new) first: test\".");
        }

        for (String s : Arrays.asList("1", "2", "3", "4", "5", "6", "7", "8", "9")) {
            client1.execute(s);
            sleep(executePause);
        }
        client3.execute("10");
        sleep(executePause);

        final String client1Answer8 = client1.getOutput().trim();
        if (client1Answer8.contains("new")) {
            return CheckResult.wrong("Client \"first\" should not mark " +
                    "it's messages with \"new\" since he's chatting with the \"second\" right now.");
        }

        String expectedMessages =
                "first: test\n" +
                        "first: 1\n" +
                        "first: 2\n" +
                        "first: 3\n" +
                        "first: 4\n" +
                        "first: 5\n" +
                        "first: 6\n" +
                        "first: 7\n" +
                        "first: 8\n" +
                        "first: 9\n" +
                        "second: 10";

        if (!client1Answer8.equals(expectedMessages)) {
            return CheckResult.wrong("Client \"first\" output wrong messages.\n\n" +
                    "Expected:\n" +
                    expectedMessages + "\n\n" +
                    "Found:\n" +
                    client1Answer8);
        }

        final String client3Answer5 = client3.getOutput().trim();
        if (client3Answer5.contains("new")) {
            return CheckResult.wrong("Client \"second\" should not mark " +
                    "it's messages with \"new\" since he's chatting with the \"first\" right now.");
        }

        expectedMessages =
                "first: 1\n" +
                        "first: 2\n" +
                        "first: 3\n" +
                        "first: 4\n" +
                        "first: 5\n" +
                        "first: 6\n" +
                        "first: 7\n" +
                        "first: 8\n" +
                        "first: 9\n" +
                        "second: 10";

        if (!client3Answer5.equals(expectedMessages)) {
            return CheckResult.wrong("Client \"second\" output wrong messages.\n\n" +
                    "Expected:\n" +
                    expectedMessages + "\n\n" +
                    "Found:\n" +
                    expectedMessages);
        }

        client1.execute("/exit");
        sleep(executePause);
        client3.execute("/exit");
        sleep(executePause);

        return CheckResult.correct();
    }

    @DynamicTestingMethod
    CheckResult test2() {
        final TestedProgram server2 = new TestedProgram("chat.server");
        final TestedProgram tempClient = new TestedProgram("chat.client");
        final TestedProgram tempClient2 = new TestedProgram("chat.client");
        tempClient.setReturnOutputAfterExecution(false);
        tempClient2.setReturnOutputAfterExecution(false);
        server2.startInBackground();
        sleep(executePause);
        tempClient.start();
        sleep(executePause);
        tempClient2.start();
        sleep(executePause);
        tempClient.getOutput();
        tempClient2.getOutput();

        tempClient.execute("/auth first 12345678");
        sleep(executePause);
        final String tempClientAnswer1 = tempClient.getOutput().trim();
        if (!tempClientAnswer1.equals("Server: you are authorized successfully!")) {
            return CheckResult.wrong("A registered client can't be authenticated after" +
                    " rebooting a server");
        }

        tempClient2.execute("/auth second 12345678");
        sleep(executePause);

        tempClient.execute("/chat second");
        sleep(executePause);
        final String tempClientAnswer2 = tempClient.getOutput().trim();

        String conversation =
                "first: 1\n" +
                        "first: 2\n" +
                        "first: 3\n" +
                        "first: 4\n" +
                        "first: 5\n" +
                        "first: 6\n" +
                        "first: 7\n" +
                        "first: 8\n" +
                        "first: 9\n" +
                        "second: 10";

        if (!tempClientAnswer2.equals(conversation)) {
            return CheckResult.wrong("Conversation between \"first\" and \"second\" " +
                    "hasn't been saved\n\n" +
                    "Expected:\n" +
                    conversation + "\n\n" +
                    "Found:\n" +
                    tempClientAnswer2);
        }

        tempClient2.execute("/exit");
        sleep(executePause);
        tempClient.execute("/exit");
        sleep(executePause);

        return CheckResult.correct();
    }

    @BeforeClass
    public static void createTempDatabaseFile() throws IOException {

        Path userDbFile = Paths.get(userDBFileName);
        Path tempUserDbFile = Paths.get(tempUserDBFileName);
        Path messageDbFile = Paths.get(messageDBFileName);
        Path tempMessageDbFile = Paths.get(tempMessageDBFileName);

        if (!userDbFile.toFile().exists())
            return;

        try {
            Files.deleteIfExists(tempUserDbFile);
            Files.move(userDbFile, tempUserDbFile);
            Files.deleteIfExists(tempMessageDbFile);
            Files.move(messageDbFile, tempMessageDbFile);
        } catch (Exception ignored) {}
    }

    @AfterClass
    public static void deleteTempDatabaseFile() throws IOException {

        Path userDbFile = Paths.get(userDBFileName);
        Path tempUserDbFile = Paths.get(tempUserDBFileName);
        Path messageDbFile = Paths.get(messageDBFileName);
        Path tempMessageDbFile = Paths.get(tempMessageDBFileName);

        if (!tempUserDbFile.toFile().exists())
            return;

        try {
            Files.deleteIfExists(userDbFile);
            Files.move(tempUserDbFile, userDbFile);
            Files.deleteIfExists(messageDbFile);
            Files.move(tempMessageDbFile, messageDbFile);
        } catch (Exception ignored) {}
    }
}