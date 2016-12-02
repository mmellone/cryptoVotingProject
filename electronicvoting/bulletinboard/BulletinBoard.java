package electronicvoting.bulletinboard;

import electronicvoting.ballot.EncryptedBallot;
import electronicvoting.paillier.PublicKey;
import electronicvoting.voter.Voter;

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
    private BigInteger d; // for the blind signature
    private PublicKey publicKey; // for Paillier
    private int A;

    public BulletinBoard(BigInteger d, PublicKey pubKey) {
        this.d = d;
        this.publicKey = pubKey;
        votes = new ArrayList<>();
        A = 10;
    }

    /**
     * Adds the ballot to the nxm votes matrix (votes)
     * @param ballot The ballot to count
     */
    public void receiveVote(EncryptedBallot ballot, Voter voter) {
        BigInteger[] vote = ballot.getEncryptedVotes();
        if (!ZKP(vote, voter)) {

        }

        votes.add(vote);

    }

    /**
     * Performs a zero-knowledge proof to make sure the voter knows the plaintext
     * @param vote The vote to check
     * @return true if the voter knows the plaintext, false otherwise
     */
    public boolean ZKP (BigInteger[] vote, Voter voter) {
        BigInteger e = new BigInteger(A, new Random());

    }


    public BigInteger[] addUpVotes () {

    }
}
