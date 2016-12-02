package electronicvoting.bulletinboard;

/**
 * Thrown if the submitted ballot's candidate list does not match the one used in a election
 */
class BallotDoesNotMatchException extends Exception {
    BallotDoesNotMatchException(String message) {
        super(message);
    }
}
