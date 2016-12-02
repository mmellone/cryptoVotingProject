package electronicvoting.ballot;

import paillierp.zkp.EncryptionZKP;

import java.math.BigInteger;

/**
 * Encrypted "ciphertext" representation of a ballot
 */
public class EncryptedBallot {
    private String[] candidates;
    private BigInteger[] votes;
    private EncryptionZKP[] zkp;
    private BigInteger signature;

    /**
     * Creates an EncryptedBallot with a list of candidates and votes
     * Declared Package-private (no modifier), can only be created by the encryptBallot function in Ballot
     *
     * @param candidates A list of candidates
     */
    EncryptedBallot(String[] candidates, BigInteger[] votes, EncryptionZKP[] zkp) {
        this.candidates = candidates.clone();
        this.votes = votes.clone();
        this.signature = null;
        this.zkp = zkp;
    }

    /**
     * Sets the blind signature for this EncryptedBallot
     *
     * @param signature The blind signature computed by the ElectionBoard class
     */
    public void setSignature(BigInteger signature) {
        this.signature = signature;
    }

    /**
     * Returns a single BigInteger representation of the votes array so that it can
     * be blind signed. This is done by simply multiplying the encrypted value of each
     * vote together
     *
     * @return Single BigInteger representation of votes array
     */
    public BigInteger signingValue() {
        BigInteger signingValue = BigInteger.ONE;
        for (BigInteger vote: votes) {
            signingValue = signingValue.multiply(vote);
        }
        return signingValue;
    }

    /**
     * @return Signature attached to this ballot
     */
    public BigInteger getSignature() {
        return signature;
    }

    /**
     * @return The list of encrypted votes
     */
    public BigInteger[] getEncryptedVotes() {
        return votes;
    }

    /**
     * @return The list of candidates
     */
    public String[] getCandidates() {
        return candidates;
    }

    /**
     * @return The attached zkp for this ballot
     */
    public EncryptionZKP[] getZKP() {
        return zkp;
    }
}
