package electronicvoting.ballot;

/**
 * Creates Ballots that have a certain list of candidates
 */
public class BallotFactory {
    private String[] candidates;

    /**
     * Creates a BallotFactory with a list of candidates
     *
     * @param candidateList The list of candidates (as a string)
     */
    public BallotFactory (String[] candidateList) {
        this.candidates = candidateList.clone();
    }

    /**
     * Creates and returns an empty ballot
     *
     * @return An empty ballot
     */
    public Ballot makeEmptyBallot() {
        return new Ballot(candidates);
    }

}
