package electronicvoting.ballot;

/**
 * Thrown if a vote is invalid
 */
public class InvalidVoteException extends Exception {
    InvalidVoteException (String message) {
        super(message);
    }
}
