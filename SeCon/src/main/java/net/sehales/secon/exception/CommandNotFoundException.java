package net.sehales.secon.exception;

public class CommandNotFoundException extends Exception {

    private static final long serialVersionUID = 4152641333264494085L;

    public CommandNotFoundException(String name) {
        super("Cannot find command \"" + name + "\"");
    }
}
