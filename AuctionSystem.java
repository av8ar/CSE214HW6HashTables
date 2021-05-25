/**
 * @author Victor Dai
 * @authorID 113206638
 * @email victor.dai.1@stonybrook.edu
 * @HWNumber
 * @course CSE 214
 * @recitation R04
 * @TA James Finn, Matthew Shinder
 */
import big.data.DataSourceException;

import java.util.*;
import java.io.*;
/**
 * This class will allow the user to interact with the database by listing open
 * auctions, make bids on open auctions, and create new auctions for different
 * items. In addition, the class should provide the functionality to load a
 * saved (serialized) AuctionTable or create a new one if a saved table does not
 * exist.
 *
 * On startup, the AuctionSystem should check to see if the file auctions.obj
 * exists in the current directory. If it does, then the file should be loaded
 * and deserialized into an AuctionTable for new auctions/bids (See the section
 * below for information on the Serializable interface). If the file does not
 * exist, an empty AuctionTable object should be created and used instead. Next,
 * the user should be promted to enter a username to access the system. This is
 * the name that will be used to create new auctions and bid on the open
 * auctions available in the table.
 *
 * When the user enters 'Q' to quit the program, the acution table should be
 * serialized to the file auctions.obj. That way, the next time the program is
 * run, the auctions will remain in the database and allow different users to
 * make bids on items. If you would like to 'reset' the auction table, simply
 * delete the auctions.obj file.
 */
public class AuctionSystem {
    private AuctionTable auctionTable;
    private String username;

    //Constructor
    public AuctionSystem() {
        auctionTable = new AuctionTable();
        username = "";
    }

    //gets the username of AuctionSystem
    public String getUsername() {
        return username;
    }

    //sets the username of AuctionSystem
    public void setUsername(String username) {
        this.username = username;
    }

    //returns auctionTable of AuctionSystem
    public AuctionTable getAuctionTable() {
        return auctionTable;
    }

    //sets the AuctionTable of AuctionSystem
    public void setAuctionTable(AuctionTable auctionTable) {
        this.auctionTable = auctionTable;
    }

    /**
     * Catches any input errors and returns a int greater than 0.
     *
     * @return int temp
     *
     * @exception InputMismatchException
     *  Indicates that the input is either not the same type as an int or
     *  that it is a negative int value.
     */
    public static int getValidInt() throws InputMismatchException {
        int temp = 0;
        Scanner d = new Scanner(System.in);
        do {
            try{
                temp = d.nextInt();
                if(temp <= 0) throw new InputMismatchException();
            }
            catch(InputMismatchException m){
                System.out.println("Input must be a numerical value greater " +
                        "than 0. Try again.");
                d.nextLine();
                continue;
            }
        }
        while(temp <= 0);
        return temp;
    }

    /**
     * Catches any input errors and returns a double that is greater than 0.0.
     *
     * @return double temp
     *
     * @exception InputMismatchException
     *  Indicates that the input is either not the same type as a double or that
     *  it is a double value less than or equal to 0.
     */
    public static Double getValidDouble() throws InputMismatchException {
        double temp = 0;
        Scanner d = new Scanner(System.in);
        temp = d.nextDouble();
        do {
            try{
                if(temp <= 0) throw new InputMismatchException();
            }
            catch(InputMismatchException m){
                System.out.println("Input must be a numerical value greater " +
                        "than 0. Try again.");
                temp = d.nextDouble();
                continue;
            }
        }
        while(temp <= 0);
        return temp;
    }

    /**
     * The method should first prompt the user for a username. This should be
     * stored in username The rest of the program will be executed on
     * behalf of this user. The following options are implemented:
     *
     * (D) - Import Data from URL
     * (A) - Create a New Auction
     * (B) - Bid on an Item
     * (I) - Get Info on Auction
     * (P) - Print All Auctions
     * (R) - Remove Expired Auctions
     * (T) - Let Time Pass
     * (Q) - Quit
     *
     */
    public static void main(String[] args) throws IOException,
            ClassNotFoundException {
        AuctionSystem system = new AuctionSystem(); //initialize AuctionSystem
        Auction auctionInfo;
        Scanner in = new Scanner(System.in);
        System.out.println("Starting...");
        AuctionTable auctions;

        try {   //previous AuctionTable file does exist
            FileInputStream file = new FileInputStream("auction.obj");
            ObjectInputStream inStream = new ObjectInputStream(file);
            auctions = (AuctionTable) inStream.readObject();
            system.setAuctionTable(auctions);
            System.out.println("Loading previous Auction Table...\n");
        }
        catch(FileNotFoundException f) { //file does not exist
            System.out.println("No previous auction table detected.\n" +
                    "Creating new table...\n");
        }

        System.out.print("Please select a username: ");
        system.setUsername(in.nextLine());
        boolean exit = false;
        String option = "";
        String url = "";
        String auctionID = "";
        double bid = 0.0;
        String auctionBid = "";
        int hours = 0;
        String itemInfo = "";
        while(!exit) {
            System.out.println("\nMenu:\n\t(D) - Import Data from URL\n\t(A) " +
                    "- " +
                    "Create a New Auction\n\t(B) - Bid on an Item\n\t(I) - Get " +
                    "Info on Auction\n\t(P) - Print All Auctions\n\t(R) - " +
                    "Remove Expired Auctions\n\t(T) - Let Time Pass\n\t(Q) - " +
                    "Quit\n\n");
            System.out.print("Please select an option: ");
            option = in.nextLine();

            switch(option.toUpperCase()) {
                case "D" :  //buildFromURL(url)
                    System.out.print("Please enter a URL: ");
                    url = in.nextLine();
                    try {
                        system.setAuctionTable(system.auctionTable.buildFromURL(url));
                        System.out.println("\nLoading...\nAuction data loaded" +
                                " successfully!");
                    }
                    catch(IllegalArgumentException i) {
                        System.out.println("Invalid URL.");
                    }
                    catch(DataSourceException d) {
                        System.out.println(d.getMessage());
                    }
                    break;
                case "A" :  //getUsername(), getValidInt()
                    System.out.println("\nCreating new Auction as " +
                            system.getUsername() + ".");
                    System.out.print("Please enter an Auction ID: ");
                    auctionID = in.nextLine();
                    System.out.print("Please enter an Auction time (hours): ");
                    hours = getValidInt();
                    System.out.print("Please enter some Item Info: ");
                    itemInfo = in.nextLine();
                    try {
                        system.getAuctionTable().put(auctionID, new Auction(hours
                                , auctionID, system.username, itemInfo));
                        System.out.println("Auction " + auctionID + " " +
                                "inserted into table.");
                    }
                    catch(IllegalArgumentException i) {
                        System.out.println("This Auction ID already exists " +
                                "and is stored in the table.");
                    }
                    break;
                case "B" :  //getAuction(auctionID), getCurrentBid(),
                    // getValidDouble(), getTimeRemaining(), newBid(username,
                    // bid)
                    System.out.print("Please enter an Auction ID: ");
                    auctionID = in.nextLine();
                    auctionInfo = system.auctionTable.getAuction(auctionID);
                    if(auctionInfo == null) {
                        System.out.println("No Auction exists with the given " +
                                "ID: " + auctionID);
                    }
                    else {
                        if(auctionInfo.getCurrentBid() == 0) auctionBid = "None";
                        else
                            auctionBid = "$ " +
                                    String.format("%.2f",
                                    auctionInfo.getCurrentBid());

                        if(auctionInfo.getTimeRemaining() == 0) {
                            System.out.println("\nAuction " + auctionID + " " +
                                    "is CLOSED\n\tCurrent Bid:  " + auctionBid +
                                    "\n\nYou can no longer bid on this item.");
                        } else {
                            System.out.println("\nAuction " + auctionID + " " +
                                            "is OPEN\n\tCurrent Bid:  " +
                                    auctionBid + "\n");
                            System.out.print("What would you like to bid?: ");
                            bid = getValidDouble();
                            System.out.println("Bid accepted.");
                            try {
                                auctionInfo.newBid(system.username, bid);
                            } catch (ClosedAuctionException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    break;
                case "I" :  //getAuction(String auctionID), getSellerName(),
                    // getBuyerName(), getTimeRemaining(), getItemInfo()
                    System.out.print("Please enter an Auction ID: ");
                    auctionID = in.nextLine();
                    if(system.auctionTable.getAuction(auctionID) == null) {
                        System.out.println("No Auction exists with the given " +
                                "ID: " + auctionID);
                    }
                    else {
                        auctionInfo = system.auctionTable.getAuction(auctionID);
                        System.out.println("Auction " + auctionID + ":\n" +
                                "\tSeller:  " + auctionInfo.getSellerName() +
                                "\n\tBuyer:  " + auctionInfo.getBuyerName() +
                                "\n\tTime:  " + auctionInfo.getTimeRemaining() +
                                " hours\n\tInfo:  " + auctionInfo.getItemInfo());
                    }
                    break;
                case "P" :  //printTable()
                    system.auctionTable.printTable();
                    break;
                case "R" :  //removeExpiredAuctions()
                    system.auctionTable.removeExpiredAuctions();
                    System.out.println("Removing expired auctions...\n" +
                            "All expired auctions removed.");
                    break;
                case "T" :  //letTimePass(hours), getValidInt()
                    System.out.print("How many hours should pass: ");
                    hours = getValidInt();
                    system.auctionTable.letTimePass(hours);
                    System.out.println("\nTime Passing...\nAuction times " +
                            "updated.");
                    break;
                case "Q" :  //Quits menu and makes auctionTable serializable
                    System.out.println("Writing Auction Table to file...\n" +
                            "Done!\n\nGoodbye.");
                    //saves file
                    FileOutputStream saveFile = new FileOutputStream(
                            "auction.obj");
                    ObjectOutputStream outStream =
                            new ObjectOutputStream(saveFile);
                    auctions = new AuctionTable();
                    auctions = system.auctionTable; //stores auctionTable in
                    // auctions
                    outStream.writeObject(auctions);
                    exit = true;
                    break;
                default :   //if user does not enter any of the appropriate
                    // options
                    System.out.println("Not a valid option. Try again.");
            } //end of switch
        } //end of menu
    } //end of main method
} //end of class AuctionSystem
