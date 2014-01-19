package net.sehales.secon.exception;

public class AddonNotFoundException extends Exception {

    private static final long serialVersionUID = -2947939648739590336L;

    public AddonNotFoundException(String name) {
        super("Cannot find addon \"" + name + "\"");
    }
}
