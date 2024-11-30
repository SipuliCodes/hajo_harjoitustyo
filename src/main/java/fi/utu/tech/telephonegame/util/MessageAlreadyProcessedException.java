package fi.utu.tech.telephonegame.util;

public class MessageAlreadyProcessedException extends Exception {
    public MessageAlreadyProcessedException(String message) {
        super(message);
    }
}
