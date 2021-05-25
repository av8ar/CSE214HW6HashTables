/**
 * @author Victor Dai
 * @authorID 113206638
 * @email victor.dai.1@stonybrook.edu
 * @HWNumber
 * @course CSE 214
 * @recitation R04
 * @TA James Finn, Matthew Shinder
 */

import java.io.*;
import java.util.Hashtable;
import big.data.DataSource;
import big.data.DataSourceException;


public class AuctionTable extends Hashtable implements Serializable {
    private Hashtable<String, Auction> auctionTable;
    //gets AuctionTable
    public Hashtable<String, Auction> getAuctionTable() {
        return auctionTable;
    }

    //sets the Hashtable auctionTable
    public void setAuctionTable(Hashtable<String, Auction> auctionTable) {
        this.auctionTable = auctionTable;
    }

    /**
     * Uses the BigData library to construct an AuctionTable from a remote
     * data source.
     *
     * <dt>
     * Preconditions:
     *      URL represents a data source which can be connected to using the
     *      BigData library.
     *      The data source has proper syntax.
     * </dt>
     *
     * @param URL - String representing the URL fo the remote data source.
     * @return The AuctionTable constructed from the remote data source.
     * @throws IllegalArgumentException
     */
    public static AuctionTable buildFromURL(String URL)
            throws IllegalArgumentException, DataSourceException {
        DataSource ds;
        try {
            ds = DataSource.connect(URL).load();
        }
        catch(IllegalArgumentException i) {
            throw new IllegalArgumentException();
        }
        catch(DataSourceException d) {
            throw new DataSourceException("Invalid URL");
        }
        AuctionTable auctionTable = new AuctionTable();
        String[] timeRemaining =
                ds.fetchStringArray("listing/auction_info/time_left");
        String[] currentBid =
                ds.fetchStringArray("listing/auction_info/current_bid");
        String[] auctionID = ds.fetchStringArray("listing/auction_info/id_num");
        String[] sellerName = ds.fetchStringArray("listing/seller_info" +
                "/seller_name");
        String[] buyerName = ds.fetchStringArray("listing/auction_info" +
                "/high_bidder/bidder_name");
        String[] memory = ds.fetchStringArray("listing/item_info/memory");
        String[] hardDrive = ds.fetchStringArray("listing/item_info" +
                "/hard_drive");
        String[] cpu = ds.fetchStringArray("listing/item_info/cpu");
        String[] itemInfo = new String[memory.length];
        for(int i = 0; i < itemInfo.length; i++) {
            itemInfo[i] = cpu[i] + " " + hardDrive[i] + " " + memory[i];
        }

        for(int i = 0; i < auctionID.length; i++) {
            if(currentBid[i].equals("")) currentBid[i] = buyerName[i] = "N/A";
            int time = 0;
            String timeLeft = timeRemaining[i];
            if(timeLeft.contains("days")) {
                time += Integer.parseInt(timeLeft.substring(0, timeLeft.indexOf(
                                "days") - 1)) * 24;
                if(timeLeft.contains(","))
                    timeLeft = timeLeft.substring(timeLeft.indexOf(",") + 2);
            }
            if(timeLeft.contains("hours")) {
                time += Integer.parseInt(timeLeft.substring(0,
                        timeLeft.indexOf("hours") - 1));
            }
            String currBid = currentBid[i].substring(1);
            if(currBid.contains(",")) currBid = currBid.substring(0,
                    currBid.indexOf(",")) + currBid.substring(
                            currBid.indexOf(",") + 1);
            Auction auctionData = new Auction(time,
                            Double.parseDouble(currBid),
                            auctionID[i], sellerName[i], buyerName[i],
                            itemInfo[i]);
            auctionTable.put(auctionID[i], auctionData);
        }
        auctionTable.setAuctionTable(auctionTable);
        return auctionTable;
    }

    /**
     * Manually posts an auction, and add it into the table.
     *
     * <dt>
     * Postconditions:
     *      The item will be added to the table if all given parameters are
     *      correct.
     * </dt>
     * @param auctionID - the unique key for this object
     * @param auction - The auction to insert into the table with the
     *                corresponding auctionID
     * @throws IllegalArgumentException
     *      Thrown if the given auctionID is already stored in the table.
     */
    public void putAuction(String auctionID, Auction auction)
            throws IllegalArgumentException {
        for(String keys : auctionTable.keySet()) {
            if(auctionID.equals(keys)) throw new IllegalArgumentException();
        }
        auctionTable.put(auctionID, auction);
    }

    /**
     * Get the information of an Auction that contains the given ID as key.
     *
     * @param auctionID - the unique key for this object
     * @return An Auction object with the given key, null otherwise.
     */
    public Auction getAuction(String auctionID) {
        return (Auction) auctionTable.get(auctionID);
    }

    /**
     * Simulates the passing of time. Decrease the timeRemaining of all Auction
     * objects by the amount specified. The value cannot go below 0.
     *
     * <dt>
     * Postconditions:
     *      All Auctions in the table have their timeRemaining timer decreased.
     *      If the original value is less than the decreased value, set the
     *      value to 0.
     * </dt>
     * @param numHours - the number of hours to decrease the timeRemaining value
     *                by.
     * @throws IllegalArgumentException : If the given numHours is non positive
     */
    public void letTimePass(int numHours) throws IllegalArgumentException {
        if(numHours <= 0) throw new IllegalArgumentException();
        for(Auction auction : auctionTable.values()) {
            auctionTable.get(auction.getAuctionID()).decrementTimeRemaining(numHours);
        }
    }

    /**
     * Iterates over all Auction objects in the table and removes them if they
     * are expired (timeRemaining == 0).
     *
     * <dt>
     * Postconditions:
     *      Only open Auction remain in the table.
     * </dt>
     */
    public void removeExpiredAuctions() {
        String[] expiredKeys = new String[auctionTable.size()];
        int i = 0;
        for(String key : auctionTable.keySet()) {
            if(auctionTable.get(key).getTimeRemaining() == 0) {
                expiredKeys[i] = key;
                i++;
            }
        }
        i = 0;
        while(i < expiredKeys.length && expiredKeys[i] != null) {
            auctionTable.remove(expiredKeys[i++]);
        }
    }

    /**
     * Prints the AuctionTable in tabular form.
     */
    public void printTable() {
        String header = String.format("%10s%3s%12s%3s%21s%3s%21s%3s%10s%3s" +
                        "%-30s",
                "Auction ID", " | ", "Bid" + (" ").repeat(3), " | ",
                "Seller" + (" ").repeat(8),
                " | ",
                "Buyer" + (" ").repeat(8),
                " | ", "Time" + (" ").repeat(3), " | ", "  Item " +
                        "Info") + "\n" + ("=").repeat(135);
        System.out.println(header);
        for(String key : auctionTable.keySet()) {
            if(auctionTable.containsKey(key)) {
                System.out.println(auctionTable.get(key).toString());
            }
        }
    }

} //end of AuctionTable class
