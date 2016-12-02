package electronicvoting.ballot;

import java.math.BigInteger;
import electronicvoting.paillier.*;

/**
 * "Plaintext" representation of a ballot
 */
public class Ballot {
    private String[] candidates;
    private int[] votes;

    /**
     * Creates a Ballot with a list of candidates
     * Declared Package-private (no modifier) so use BallotFactory to create Ballots
     *
     * @param candidates A list of candidates
     */
    Ballot(String[] candidates) {
        this.candidates = candidates.clone();
        this.votes = null;

    }

    /**
     * Vote for a candidate name (string)
     *
     * @param candidateName The candidates name, must be valid
     */
    public void vote(String candidateName) throws InvalidVoteException {
        int index = -1;
        for (int i = 0; i < candidates.length; i++) {
            if (candidates[i].equals(candidateName)) {
                index = i;
            }
        }

        this.vote(index);
    }

    /**
     * Vote for a candidate number
     *
     * @param candidate The number of the candidate
     */
    private void vote(int candidate) throws InvalidVoteException {
        if (candidate < 0 || candidate > candidates.length) {
            throw new InvalidVoteException("Candidate is not on the ballot");
        }

        votes = new int[candidates.length];
        for (int i = 0; i < votes.length; i++) {
            if (i == candidate) {
                votes[i] = 1;
            } else {
                votes[i] = 0;
            }
        }
    }

    /**
     * Encrypts all of the votes on the ballot and returns
     * @param publicKey
     * @return
     */
    public EncryptedBallot encryptVote(PublicKey publicKey) {
        BigInteger[] encryptedVotes = new BigInteger[votes.length];

        for (int i = 0; i < votes.length; i++) {
            BigInteger ctxt;
            if (votes[i] == 1) {
                ctxt = Paillier.encrypt(BigInteger.ONE, publicKey);
            } else {
                ctxt = Paillier.encrypt(BigInteger.ZERO, publicKey);
            }
            encryptedVotes[i] = ctxt;
        }
        return new EncryptedBallot(candidates, encryptedVotes);
    }
}