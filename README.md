# cryptoVotingProject
Melissa's and Mitchell's E-Voting project for Prof Yener's Fall 2016 Crypto Class

This project uses the UT-Dallas Paillier Encryption library, which is licensed under GPL v3 
(https://www.gnu.org/copyleft/gpl.html) and has documentation which can be found at
http://cs.utdallas.edu/dspl/cgi-bin/pailliertoolbox/. Additionally all of the source
code from this library is paillierp package. Although we really only used the Paillier, PaillierKey,
KeyGen, and EncryptionZKP classes from this library.

All of the code that we wrote ourselves is contained in the electronicvoting package.
It is split up into 4 subpackages which pertain to the voter, election board, bulletin board,
and ballots. The high level use case would be to instantiate a bunch of Voter objects
(each with a unique id), and a single election board and bulletin board. The Voters then
register with the election board and can cast a ballot in via the election board they are
registered with (the ElectionBoard class generates specific blank ballots for its election
with a list of candidates it was constructed with). The Voter can then vote by getting a
ballot from the ElectionBoard, selecting a candidate, encrypting their vote, getting it
blind signed, and then submitting it to the BulletinBoard for counting; all of this work
is contained in the Voter.vote() method. The BulletinBoard will then check each vote to
make sure it is valid, and count up the encrypted votes. This encrypted count can be
submitted to the ElectionBoard (which holds the Paillier Private key) to be decrypted.

A simple example is shown in the main method of the VotingMain class. To run, simply cd
into the top level directory of this project and run "javac VotingMain.java" and then
"java VotingMain".

We assume that the only mechanism in which someone can vote is though the Voter and
Ballot classes that we created.

If you have any questions email Mitchell at mellom3@rpi.edu


