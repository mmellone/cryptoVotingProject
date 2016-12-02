package electronicvoting.paillier;

import java.math.BigInteger;

/**
 * Represents the publicKey in a paillier encryption scheme
 */
public class PublicKey {
    BigInteger n, g;

    /**
     * Holds information on the public key
     *
     * @param n p*q
     * @param g Random integer less than and relatively prime to n^2
     */
    PublicKey(BigInteger n, BigInteger g) {
        this.n = n;
        this.g = g;
    }

    /**
     * @return Copy of this
     */
    public PublicKey clone() {
        return new PublicKey(n, g);
    }
}