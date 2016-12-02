package electronicvoting.bulletinboard;

/**
 * Thrown if the ZKP validation fails
 */
class FailedZKPException extends Exception {
    FailedZKPException(String message) {
        super(message);
    }
}
