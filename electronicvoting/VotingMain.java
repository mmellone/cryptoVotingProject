package ElectronicVoting;

import ElectronicVoting.paillier.Paillier;

import java.math.BigInteger;

/**
 * Created by mitchell on 11/29/16.
 */
public class VotingMain {
    public static void main(String args[]) {
        KeyPair keys = Paillier.generateKeyPair();
        BigInteger m = new BigInteger("20");
        System.out.println("Orig m = " + m.toString());
        BigInteger c = Paillier.encrypt(m, keys.publicKey);
        System.out.println("Ctxt = " + c.toString());
        BigInteger ptxt = Paillier.decrypt(c, keys.privateKey);
        System.out.println("Ptxt = " + ptxt.toString());

    }
}
