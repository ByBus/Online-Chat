package chat.server.command;

public abstract class CommandInputChecks {
    protected final String pattern;
    protected final int parametersNumber;

    public CommandInputChecks(String pattern, int parametersNumber) {
        this.pattern = pattern;
        this.parametersNumber = parametersNumber + 1;
    }

    protected boolean checkPattern(String input) {
        return input.startsWith(pattern);
    }

    protected boolean checkIncorrectParamsNumber(String[] params) {
        return params.length != parametersNumber;
    }

    protected String[] parameters(String command) {
        return command.trim().split("\\s+");
    }
}
