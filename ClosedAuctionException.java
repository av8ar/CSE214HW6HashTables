/**
 * @author Victor Dai
 * @authorID 113206638
 * @email victor.dai.1@stonybrook.edu
 * @HWNumber 6
 * @course CSE 214
 * @recitation R04
 * @TA James Finn, Matthew Shinder
 */

/**
 * ClosedAuctionException is a custom Exception that extends Exception and
 * indicates that the Auction is closed and timeRemaining = 0
 */
public class ClosedAuctionException extends Exception {
    public ClosedAuctionException(){} //Default
    public ClosedAuctionException(String s) { //Sends error message
        super(s);
    }
}
