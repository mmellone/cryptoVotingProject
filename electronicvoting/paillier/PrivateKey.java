package electronicvoting.paillier;

import java.math.BigInteger;

/**
 * Represents a Private key in the Paillier encryption scheme
 */
public class PrivateKey {
    BigInteger lambda, mu, n;

    /**
     * Holds information on the private key
     *
     * @param lambda lcm(p-1, q-1)
     * @param mu = (L(g^lambda mod n^2))^-1 mod n
     * @param n pq
     */
    PrivateKey(BigInteger lambda, BigInteger mu, BigInteger n) {
        this.lambda = lambda;
        this.mu = mu;
        this.n = n;
    }

    /**
     * @return Copy of this
     */
    public PrivateKey clone() {
        return new PrivateKey(lambda, mu, n);
    }
}