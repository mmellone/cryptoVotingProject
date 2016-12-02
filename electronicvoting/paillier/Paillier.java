package electronicvoting.paillier;

import java.math.BigInteger;
import java.util.Random;


public class Paillier {
    private static final int KEY_SIZE = 16;

    /**
     * Generates a public-private key pair for Paillier Encryption
     *
     * @return public-private key pair
     */
    public static PaillierKeyPair generateKeyPair() {
        // Select 2 large prime numbers
        BigInteger p, q;
        Random rnd = new Random();
        do {
            p = BigInteger.probablePrime(KEY_SIZE/2, rnd);
            q = BigInteger.probablePrime(KEY_SIZE/2, rnd);
        } while (!((p.multiply(q)).gcd(p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE))).equals(BigInteger.ONE)));

        // Compute n and lambda
        BigInteger n = p.multiply(q); // = p*q
        BigInteger pMinus1 = p.subtract(BigInteger.ONE);
        BigInteger qMinus1 = q.subtract(BigInteger.ONE);
        BigInteger lambda = (pMinus1.multiply(qMinus1)).divide(pMinus1.gcd(qMinus1)); // = lcm(p-1, q-1) = (p-1)(q-1) / gcd(p-1, q-1)

        // Select g
        BigInteger g, mu;
        mu = BigInteger.ONE; // initializing mu to null to suppress a 'may not be assigned' warning
        BigInteger nSquared = n.pow(2);
        boolean invertible;
        do {
            do {
                g = new BigInteger(nSquared.bitLength(), rnd);
            } while (!(g.compareTo(nSquared) < 0 && g.gcd(nSquared).equals(BigInteger.ONE)));
            invertible = true;
            try {
                mu = calcMu(g, lambda, n, nSquared);
            } catch (ArithmeticException e) {
                invertible = false;
            }
        } while (!invertible);

        if (g.modPow(lambda, nSquared).subtract(BigInteger.ONE).divide(n).gcd(n).intValue() != 1) {
            System.out.println("g is not good. Choose g again.");
            System.exit(1);
        }

        PublicKey publicKey = new PublicKey(n, g);
        PrivateKey privateKey = new PrivateKey(lambda, mu, n);

        return new PaillierKeyPair(publicKey, privateKey);
    }

    /**
     * Calculates the value of mu
     *
     * @param g Random integer less than and relatively prime to n^2
     * @param lambda lcm(p-1, q-1)
     * @param n pq
     * @param nSquared n^2
     * @return mu = (L(g^lambda mod n^2))^-1 mod n
     */
    private static BigInteger calcMu(BigInteger g, BigInteger lambda, BigInteger n, BigInteger nSquared) throws ArithmeticException {
        BigInteger x = g.modPow(lambda, nSquared);
        BigInteger LRes = L(x, n);
        return LRes.modInverse(n);
    }

    /**
     * Returns the value of L(x)
     *
     * @param x The parameter to the function L
     * @param n pq
     * @return x-1 / n
     */
    private static BigInteger L(BigInteger x, BigInteger n) {
        return (x.subtract(BigInteger.ONE)).divide(n);
    }

    /**
     * Encrypts plain-text m using public key and returns the cipher-text
     *
     * @param m Plain-text message
     * @param publicKey Paillier public key
     * @return The cipher-text c
     */
    public static BigInteger encrypt(BigInteger m, PublicKey publicKey) {
        Random rnd = new Random();
        BigInteger r; // generate r to be less than and relatively prime to n
        do {
            r = new BigInteger(publicKey.n.bitLength(), rnd);
        } while (!(r.compareTo(publicKey.n) < 0 && r.gcd(publicKey.n).equals(BigInteger.ONE)));
        BigInteger nSquared = publicKey.n.pow(2);

        return ((publicKey.g.modPow(m, nSquared)).multiply(r.modPow(publicKey.n, nSquared))).mod(nSquared);
    }

    /**
     * Decrypts cipher-text c using private key and returns the plain-text
     *
     * @param c Cipher-text encryption
     * @param privateKey Paillier private key
     * @return The plain-text m
     */
    public static BigInteger decrypt(BigInteger c, PrivateKey privateKey) {
        BigInteger nSquared = privateKey.n.pow(2);
        BigInteger x = c.modPow(privateKey.lambda, nSquared);
        return (L(x, privateKey.n).multiply(privateKey.mu)).mod(privateKey.n);
    }
}