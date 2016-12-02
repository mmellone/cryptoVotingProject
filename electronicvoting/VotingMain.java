package electronicvoting;


import electronicvoting.bulletinboard.BulletinBoard;
import electronicvoting.eb.ElectionBoard;
import electronicvoting.eb.AlreadyRegisteredException;
import electronicvoting.voter.Voter;
import paillierp.*;
import paillierp.key.*;
import paillierp.zkp.EncryptionZKP;

import java.math.BigInteger;
import java.util.Random;

/**
 * Created by mitchell on 11/29/16.
 */
public class VotingMain {
    public static void main(String args[]) {
        String[] candidates = new String[]{"bob", "alice", "yener"};
        ElectionBoard eb = new ElectionBoard(candidates);
        BulletinBoard bb = new BulletinBoard();
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

//        Random gen = new Random();
//        PaillierPrivateKey pKey = KeyGen.PaillierKey(128, gen.nextLong());
//        Paillier publicPaillier = new Paillier(pKey.getPublicKey());
//        Paillier privatePaillier = new Paillier(pKey.getPublicKey());
//        privatePaillier.setDecryption(pKey);
//
//        BigInteger m = new BigInteger("20");
//        System.out.println("Orig m = " + m.toString());
//        BigInteger c = publicPaillier.encrypt(m);
//        System.out.println("Ctxt = " + c.toString());
//        EncryptionZKP zkp = publicPaillier.encryptProof(m);
//        System.out.println("ZKP = " + zkp.verifyKey(pKey.getPublicKey()));
//        BigInteger ptxt = privatePaillier.decrypt(c);
//        System.out.println("Ptxt = " + ptxt.toString());

    }
}
