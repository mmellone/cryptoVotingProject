package electronicvoting.eb;


import electronicvoting.ballot.Ballot;
import electronicvoting.ballot.BallotFactory;
import electronicvoting.bulletinboard.BulletinBoard;
import electronicvoting.voter.Voter;
import paillierp.KeyGen;
import paillierp.Paillier;
import paillierp.key.PaillierKey;
import paillierp.key.PaillierPrivateKey;

import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ElectionBoard {
    /**
     * Map of voterIDs (integers) to a boolean stating whether or not they voted
     */
    private Map<Integer, Boolean> hasVotedList;
    private BallotFactory ballotFactory;
    private Paillier publicPaillier;
    private Paillier privatePaillier;
    private BigInteger n, e, d; // RSA keys for blind signature



    /**
     * Constructs an ElectionBoard with empty list of voters, and initializes
     * the RSA keys for blind signing, the ballot factory to distribute ballots
     * to voters, and the Paillier keys to encrypt the vote.
     *
     * @param candidates The candidates in this election
     */
    public ElectionBoard(String[] candidates) {
        hasVotedList = new HashMap<>();
        generateRSAKeys();
        ballotFactory = new BallotFactory(candidates);
        Random gen = new Random();
        PaillierPrivateKey pKey = KeyGen.PaillierKey(128, gen.nextLong());
        publicPaillier = new Paillier(pKey.getPublicKey());
        privatePaillier = new Paillier(pKey.getPublicKey());
        privatePaillier.setDecryption(pKey);
    }

    /**
     * Generates the RSA keys for blind signing
     */
    private void generateRSAKeys() {
        KeyPair kp;
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            kp = kpg.genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            kp = null;
            System.out.println("Could not generate key pair");
            System.exit(-1);
        }
        RSAPublicKeySpec pubKey;
        RSAPrivateKeySpec privKey;
        try {
            KeyFactory fact = KeyFactory.getInstance("RSA");
            pubKey = fact.getKeySpec(kp.getPublic(), RSAPublicKeySpec.class);
            privKey = fact.getKeySpec(kp.getPrivate(),
                    RSAPrivateKeySpec.class);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            pubKey = null;
            privKey = null;
            System.out.println("Could not generate key pair");
            System.exit(-1);
        }
        n = pubKey.getModulus();
        assert(n.equals(privKey.getModulus()));
        e = pubKey.getPublicExponent();
        d = privKey.getPrivateExponent();
    }

    public Ballot getNewBallot() {
        return ballotFactory.makeEmptyBallot();
    }

    /**
     * @return The paillier public encryption service
     */
    public Paillier getPublicPaillier() {
        return publicPaillier;
    }

    /**
     * @return The RSA public key for the blind signature
     */
    public BigInteger getBlindSignKey() {
        return e;
    }

    /**
     * @return The RSA n value for the blind signature
     */
    public BigInteger getBlindSignN() {
        return n;
    }

    /**
     * @return The N value used for Paillier encryption
     */
    public BigInteger getN() {
        return publicPaillier.getPublicKey().getN();
    }


    /**
     * Registers a voter with this election board
     *
     * @param newVoter The new voter to register
     * @throws AlreadyRegisteredException If the voter has already been registered (matching ids)
     */
    public void registerVoter(Voter newVoter) throws AlreadyRegisteredException {
        if (hasVotedList.containsKey(newVoter.getID())) {
            throw new AlreadyRegisteredException("Voter " + newVoter.getID() + " has already registered.");
        }
        hasVotedList.put(newVoter.getID(), false);
    }

    
    public BigInteger blindSign(BigInteger blindVote, int voterID) throws VotingException{
        if (hasVotedList.get(voterID) == null) {
            // If the voter is not registered
            throw new VotingException("The voter has not registered!");
        } else if (hasVotedList.get(voterID)) {
            // If the voter has already voted
            throw new VotingException("The voter has already voted!");
        }
        // If the voter is registered and not already voted then mark them as voted and return the signature
        hasVotedList.put(voterID, true);
        return blindVote.modPow(d, n);
    }

    public BigInteger[] decryptResults(BigInteger[] encryptedResults) {
        BigInteger[] results = new BigInteger[encryptedResults.length];
        for (int i = 0; i < encryptedResults.length; i++) {
            results[i] = privatePaillier.decrypt(encryptedResults[i]);
//            System.out.println("Result: " + results[i]);
        }
        return results;
    }
}


