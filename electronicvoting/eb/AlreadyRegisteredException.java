package electronicvoting.eb;

/**
 * Thrown if a voter has already been registered
 */
public class AlreadyRegisteredException extends Exception {
    AlreadyRegisteredException(String message) {
        super(message);
    }
}
