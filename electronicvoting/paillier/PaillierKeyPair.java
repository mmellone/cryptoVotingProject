package electronicvoting.paillier;

public class PaillierKeyPair {
    public PublicKey publicKey;
    public PrivateKey privateKey;

    /**
     * Holds a pair of public and private keys for Paillier Encryption
     *
     * @param publicKey The public key (n, g)
     * @param privateKey The private key (lambda, mu)
     */
    PaillierKeyPair(PublicKey publicKey, PrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }
}