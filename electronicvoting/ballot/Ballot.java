package electronicvoting.ballot;

import paillierp.Paillier;
import paillierp.zkp.EncryptionZKP;

import java.math.BigInteger;

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
     * @param encryptionService The service used to encrypt the votes
     * @return An encrypted array of votes
     */
    public EncryptedBallot encryptVote(Paillier encryptionService) {
        BigInteger[] encryptedVotes = new BigInteger[votes.length];
        EncryptionZKP[] zkp = new EncryptionZKP[votes.length];
        for (int i = 0; i < votes.length; i++) {
            if (votes[i] == 1) {
                encryptedVotes[i] = encryptionService.encrypt(BigInteger.ONE);
                zkp[i] = encryptionService.encryptProof(BigInteger.ONE);
            } else {
                encryptedVotes[i] = encryptionService.encrypt(BigInteger.ZERO);
                zkp[i] = encryptionService.encryptProof(BigInteger.ZERO);
            }
        }
        return new EncryptedBallot(candidates, encryptedVotes, zkp);
    }
}