/**
 * @author Victor Dai
 * @authorID 113206638
 * @email victor.dai.1@stonybrook.edu
 * @HWNumber 6
 * @course CSE 214
 * @recitation R04
 * @TA James Finn, Matthew Shinder
 */
import java.io.Serializable;
/**
 * Auction represents an active auction currently in the database.
 */
public class Auction implements Serializable {
    //Instantiation of member variables
    private int timeRemaining;
    private double currentBid;
    private String auctionID, sellerName, buyerName, itemInfo;

    //Constructors (Default)
    public Auction(){}
    //Overloaded Constructors
    public Auction(int timeRemaining, String auctionID,
                   String sellerName, String itemInfo) {
        this.timeRemaining = timeRemaining;
        this.auctionID = auctionID;
        this.sellerName = sellerName;
        this.itemInfo = itemInfo;
        currentBid = 0.0;
        buyerName = "N/A";
    }

    public Auction(int timeRemaining, double currentBid, String auctionID,
                   String sellerName, String buyerName, String itemInfo) {
        this.timeRemaining = timeRemaining;
        this.currentBid = currentBid;
        this.auctionID = auctionID;
        this.sellerName = sellerName;
        this.buyerName = buyerName;
        this.itemInfo = itemInfo;
    }

    //Gets the timeRemaining for an Auction object
    public int getTimeRemaining() {
        return timeRemaining;
    }
    //Gets the currentBid for an Auction object
    public double getCurrentBid() {
        return currentBid;
    }
    //Gets the ID of the Auction object
    public String getAuctionID() {
        return auctionID;
    }
    //Gets the sellerName of the Auction object
    public String getSellerName() {
        return sellerName;
    }
    //Gets the buyerName of the Auction object
    public String getBuyerName() {
        return buyerName;
    }
    //Gets the itemInfo of the Auction object
    public String getItemInfo() {
        return itemInfo;
    }

    //Methods of Auction

    /**
     * Decreases the time remaining for this auction by the specified amount.
     * If time is greater than the current remaining time for the auction, then
     * the time remaining is set to 0 (i.e. no negative times).
     *
     * <dt>
     * Postconditiion:
     *      timeRemaining has been decremented by the indicated amount and is
     *      greater than or equal to 0.
     * </dt>
     *
     * @param time - the time being decremented from timeRemaining
     */
    public void decrementTimeRemaining(int time) throws IllegalArgumentException
    {
        if(time < 0) throw new IllegalArgumentException();
        if(time > timeRemaining) timeRemaining = 0; //Ensure no negative times
        else timeRemaining -= time;
    }

    /**
     * Makes a new bid on this auction. If bidAmt is larger than currentBid,
     * then the value of currentBid is replaced by bidAmt and buyerName is is
     * replaced by bidderName.
     * <dt>
     * Precondition:
     *      The auction is not closed (i.e. timeRemaining > 0).
     * </dt>
     *
     * <dt>
     * Postcondition:
     *      currentBid Reflects the largest bid placed on this object. If the
     *      auction is closed, throw a ClosedAuctionException.
     * </dt>
     *
     * @param bidderName - name of new bidder
     * @param bidAmt - amount of new bidder
     * @throws ClosedAuctionException - Thrown if the auction is closed and no
     * more bids can be placed (i.e. timeRemaining == 0).
     */
    public void newBid(String bidderName, double bidAmt)
            throws ClosedAuctionException {
        if(timeRemaining == 0) throw new ClosedAuctionException();
        if(bidAmt > currentBid) {
            currentBid = bidAmt;
            buyerName = bidderName;
        }
    }

    /**
     * Returns string of data members in tabular form.
     *
     * @return String of data members of Auction object in tabular form.
     */
    public String toString() {
        String bid = (" ").repeat(12);
        String buyer = (" ").repeat(10);
        if(currentBid != 0.0) {
            bid = "$\t" + String.format("%.2f", currentBid);
            buyer = buyerName;
        }
        String trimmedItemInfo = itemInfo;
        if(itemInfo.length() > 50) trimmedItemInfo = itemInfo.substring(0,44);

        return String.format("%10s%3s%-11s%3s%-21s%3s%-21s%3s%10s%3s%-30s",
                auctionID, " | ", bid,
                " | ", sellerName, " | ", buyer, " | ",
                Integer.toString(timeRemaining) + " hours", " | ",
                trimmedItemInfo);
    }
} //end of Auction class
