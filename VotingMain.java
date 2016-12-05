import electronicvoting.bulletinboard.BulletinBoard;
import electronicvoting.eb.ElectionBoard;
import electronicvoting.eb.AlreadyRegisteredException;
import electronicvoting.voter.Voter;

import java.math.BigInteger;

/**
 * Example of how to use voting system
 */
public class VotingMain {
    public static void main(String args[]) {
        String[] candidates = new String[]{"bob", "alice", "yener"};
        ElectionBoard eb = new ElectionBoard(candidates);
        BulletinBoard bb = new BulletinBoard(candidates);
        Voter v1 = new Voter(1);
        Voter v2 = new Voter(2);
        Voter v3 = new Voter(3);
        try {
            eb.registerVoter(v1);
            eb.registerVoter(v2);
            eb.registerVoter(v3);
        } catch (AlreadyRegisteredException e) {
            System.out.println(e.getMessage());
            return;
        }
        v1.vote(eb, bb, "bob");
        v2.vote(eb, bb, "yener");
        v3.vote(eb, bb, "yener");
        BigInteger[] encryptedResults = bb.countVotes(eb.getN());
        BigInteger[] decryptedResults = eb.decryptResults(encryptedResults);

        for (int i = 0; i < encryptedResults.length; i++) {
            System.out.println(candidates[i] + ": " + decryptedResults[i] + " votes");
        }
    }
}
