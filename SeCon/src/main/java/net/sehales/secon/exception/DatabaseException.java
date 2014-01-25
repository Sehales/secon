package net.sehales.secon.exception;

public class DatabaseException extends Exception {

    private static final long serialVersionUID = -3456429280459510485L;

    public DatabaseException(String error) {
        super(error);
    }
}
