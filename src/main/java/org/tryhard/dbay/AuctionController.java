package org.tryhard.dbay;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.tryhard.dbay.exceptions.NoStealingException;
import org.tryhard.dbay.exceptions.UnknownCurrencyException;
import org.tryhard.dbay.money.Money;
import org.tryhard.dbay.money.MoneyUtil;
import org.tryhard.dbay.money.impls.USD;
import org.tryhard.dbay.objects.Auction;
import org.tryhard.dbay.objects.Bid;
import org.tryhard.dbay.objects.User;

/**
 * 
 * @author Daniel Tsukiji
 */
@RestController
public class AuctionController
{
    private final ConcurrentHashMap<Integer, Auction> AUCTIONS = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, User> USERS = new ConcurrentHashMap<>();
    private static AuctionController singleton = null;
    private static final Money STAPLER_REPLACEMENT_FUND = new USD();
    
    private AuctionController(){}
    
    public static AuctionController getAuctionController()
    {
        if(singleton == null)
            singleton = new AuctionController();
        return singleton;
    }
    
    private User getUser(String email)
    {
        return USERS.get(new User(email).hashCode());
    }
    
    private Auction getAuction(User user, String itemName)
    {
        return AUCTIONS.get(new Auction(user, itemName, Instant.now()).hashCode());
    }
    
    @RequestMapping("/addNewAuction")
    public String addNewAuction(@RequestParam("ownerEmail") String email,
            @RequestParam("itemName") String itemName)
    {
        Instant now = Instant.now();
        User user = getUser(email);
        Auction auction;
        
        if(user == null)
            return "Could not find " + email + " in the user list.";
        
        auction = new Auction(user, itemName, now);
        
        if(AUCTIONS.containsKey(auction.hashCode()))
            return email + " already auctioning " + itemName;
        
        AUCTIONS.put(auction.hashCode(), auction);
        return "Auction added!";
    }
    
    @RequestMapping("/placeBid")
    public String placeBid(@RequestParam("ownerEmail") String email,
            @RequestParam("itemName") String itemName,
            @RequestParam("amount") double amount,
            @RequestParam("currency") String currency,
            @RequestParam("bidderEmail") String bidderEmail)
    {
        Instant now = Instant.now();
        User bidder = getUser(bidderEmail), owner = getUser(email);
        Auction auction;
        Money money;
        Bid bid;
        
        if(bidder == null)
            return "Unable to find user attached to " + bidderEmail;
        if(owner == null)
            return "Unable to find user attached to " + email;
        if(owner.equals(bidder))
            return "You can't get high on your own supply!";
        
        auction = getAuction(owner, itemName);
        
        if(auction == null)
            return "Couldn't find auction.";
        if(auction.getEndsAt().compareTo(now) < 0)
            return "Auction has already ended, winner: " + auction.getHighest();
        
        try
        {
            money = MoneyUtil.makeMoney(amount, currency);
            money.setOwnedBy(bidder);
        }
        catch(UnknownCurrencyException | NoStealingException ex)
        {
            return ex.getMessage();
        }
        bid = new Bid(money, bidder);
        
        if(auction.addBid(bid))
            return "New high bid!";
        
        return "Not... good... enough!";
    }
    
    @RequestMapping("/endAuction")
    public String endAuction(@RequestParam("ownerEmail") String email,
            @RequestParam("itemName") String itemName)
    {
        Instant now = Instant.now();
        User owner = getUser(email);
        Auction auction = getAuction(owner, itemName);
        
        if(owner == null)
            return "Unable to associate a user with " + email;
        if(auction == null)
            return "Unable to find an auction from " + email + " of " + itemName;
        if(auction.getEndsAt().compareTo(now) < 0)
            return "Auction has already ended, winner:\n" + auction.getHighest();
        
        auction.endBidding();
        return "Auction ended, winner:\n" + auction.getHighest();
    }
    
    @RequestMapping("/addUser")
    public String addUser(@RequestParam("lastName") String lastName,
            @RequestParam("firstName") String firstName,
            @RequestParam("email") String email,
            @RequestParam("amount") double amount,
            @RequestParam("currency") String currency)
    {
        Money money;
        User user;
        
        try
        {
            money = MoneyUtil.makeMoney(amount, currency);
        }
        catch(UnknownCurrencyException uce)
        {
            return uce.getMessage();
        }
        
        user = new User(lastName, firstName, email, money);
        
        if(USERS.containsKey(user.hashCode()))
            return "User already exists.";
        
        USERS.put(user.hashCode(), user);
        return "User added.";
    }
    
    public static void takeAPennyLeaveAPenny(BigDecimal fractionsOfAPenny,
            Currency currency)
    {
        STAPLER_REPLACEMENT_FUND.addOnSly(fractionsOfAPenny, currency);
    }
}
