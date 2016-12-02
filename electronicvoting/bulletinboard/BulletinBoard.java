package electronicvoting.bulletinboard;

import electronicvoting.ballot.EncryptedBallot;
import electronicvoting.eb.ElectionBoard;
import paillierp.zkp.EncryptionZKP;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Represents a table of all encrypted votes.
 * Performs ZKP to check for validity and adds votes up
 */
public class BulletinBoard {
    private List<BigInteger[]> votes;
    private String[] candidates;

    /**
     * Creates an empty bulletin board
     */
    public BulletinBoard(String[] candidates) {
        votes = new ArrayList<>();
        this.candidates = candidates;
    }

    /**
     * Adds the ballot to the nxm votes matrix (votes)
     *
     * @param ballot The ballot to count
     */
    public void receiveVote(EncryptedBallot ballot, ElectionBoard eb) throws FailedZKPException, BallotDoesNotMatchException {
        BigInteger[] vote = ballot.getEncryptedVotes();
        eb.checkSignature(ballot.signingValue(), ballot.getSignature());
        EncryptionZKP[] zkp = ballot.getZKP();
        if (!Arrays.equals(ballot.getCandidates(), candidates)) {
            throw new BallotDoesNotMatchException("The submitted ballot does not match this election");
        }
        for (EncryptionZKP p : zkp) {
            if (!p.verify()) {
                throw new FailedZKPException("The ZKP failed for this voter");
            }
        }
        votes.add(vote);
    }

    /**
     * Count up the encrypted votes
     *
     * @param N The N used in the Paillier Encryption
     * @return An array of encrypted sums of votes for each candidate
     */
    public BigInteger[] countVotes(BigInteger N) {
        if (votes.size() == 0) {
            return null;
        }
        BigInteger[] sums = votes.get(0);
        for (int i = 1; i < votes.size(); i++) {
            for (int j = 0; j < votes.get(i).length; j++) {
                sums[j] = sums[j].multiply(votes.get(i)[j]).mod(N.pow(2));

            }

        }

        return sums;
    }
}
