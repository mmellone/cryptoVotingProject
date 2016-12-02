package electronicvoting.eb;

/**
 * Thrown if there is an issue in the voting process
 */
public class VotingException extends Exception{
    VotingException(String message) {
        super(message);
    }
}
