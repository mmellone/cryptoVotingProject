package electronicvoting.bulletinboard;

import electronicvoting.ballot.EncryptedBallot;
import electronicvoting.paillier.PublicKey;
import electronicvoting.voter.Voter;
import paillierp.key.PaillierKey;
import paillierp.zkp.EncryptionZKP;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a table of all encrypted votes.
 * Performs ZKP to check for validity and adds votes up
 */
public class BulletinBoard {
    private List<BigInteger[]> votes;


    public BulletinBoard() {
        votes = new ArrayList<>();
    }

    /**
     * Adds the ballot to the nxm votes matrix (votes)
     * @param ballot The ballot to count
     */
    public void receiveVote(EncryptedBallot ballot, Voter voter) throws FailedZKPException {
        BigInteger[] vote = ballot.getEncryptedVotes();
        System.out.println("Voter " + voter.getID());
        for (BigInteger v: vote) {
            System.out.print("\tVote: " + v);
        }
        System.out.println();
        EncryptionZKP[] zkp = ballot.getZKP();
        for (EncryptionZKP p : zkp) {
            if (!p.verify()) {
                throw new FailedZKPException("The ZKP failed for this voter");
            }
        }
        votes.add(vote);

    }


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
        for (BigInteger sum : sums) {
            System.out.println("Sum = " + sum);
        }
        return sums;
    }
}
