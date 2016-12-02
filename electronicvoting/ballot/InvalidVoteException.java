package electronicvoting.ballot;

/**
 * Created by mitchell on 12/1/16.
 */
public class InvalidVoteException extends Exception {
    public InvalidVoteException (String message) {
        super(message);
    }
}
