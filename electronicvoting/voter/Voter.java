package electronicvoting.voter;


import electronicvoting.ballot.Ballot;
import electronicvoting.ballot.EncryptedBallot;
import electronicvoting.ballot.InvalidVoteException;
import electronicvoting.bulletinboard.BulletinBoard;
import electronicvoting.bulletinboard.FailedZKPException;
import electronicvoting.eb.ElectionBoard;
import electronicvoting.eb.VotingException;

import java.math.BigInteger;
import java.util.Random;


public class Voter {
    private int id;

    /**
     * Creates a voter with a unique ID and r (for blindSigning)
     *
     * @param id The unique voter ID
     */
    public Voter(int id) {
        this.id = id;
    }

    /**
     * Returns the voter's ID
     *
     * @return Voter ID
     */
    public int getID() {
        return id;
    }

    /**
     * Lets the voter vote for candidate choice in the election governed by eb
     *
     * @param eb The ElectionBoard the voter is registered at
     * @param candidateChoice The voters choice of candidate
     */
    public void vote(ElectionBoard eb, BulletinBoard bb, String candidateChoice) {
        Ballot myBallot = eb.getNewBallot();
        try {
            myBallot.vote(candidateChoice);
        } catch (InvalidVoteException e) {
            System.out.println(e.getMessage());
            return;
        }

        // Encrypt vote and get it blind signed
        EncryptedBallot myEBallot = myBallot.encryptVote(eb.getPublicPaillier());
        BigInteger blindSig;
        try {
            BigInteger r = new BigInteger(2048, new Random());
            BigInteger blindSignMsg = myEBallot.signingValue().multiply(r.modPow(eb.getBlindSignKey(), eb.getBlindSignN()));
            blindSig = eb.blindSign(blindSignMsg, id);
            blindSig = blindSig.divide(r);
        } catch (VotingException e) {
            System.out.println(e.getMessage());
            return;
        }
        myEBallot.setSignature(blindSig);

        try {
            bb.receiveVote(myEBallot, this);
        } catch (FailedZKPException e) {
            System.out.println(e.getMessage());
        }
    }

}
