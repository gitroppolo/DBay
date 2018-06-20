package org.tryhard.dbay.objects;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Stack;
import org.tryhard.dbay.exceptions.NoMoreFunException;
import org.tryhard.dbay.money.interfaces.IWentToVegas;

/**
 *
 * @author Daniel Tsukiji
 */
public class Auction
{
    private Bid highest;
    private User auctionedBy;
    private Stack<Bid> previousHighBids;
    private Instant startedAt, endsAt;
    private String itemName;
    
    public Auction(User auctionedBy, String itemName, Instant startedAt)
    {
        this.auctionedBy = auctionedBy;
        this.itemName = itemName;
        this.startedAt = startedAt;
        endsAt = startedAt.plus(Duration.ofMinutes(30));
        previousHighBids = new Stack<>();
    }

    public boolean addBid(Bid bid)
    {
        if(highest == null)
        {
            highest = bid;
            return true;
        }
        
        if(bid.getBidder().equals(highest.getBidder())
                && highest.getMoney() instanceof IWentToVegas)
        {
            try
            {
                boolean won = ((IWentToVegas)highest.getMoney()).spinTheWheel();
                return won;
            }
            catch(NoMoreFunException nmfe)
            {
                if(previousHighBids.empty())
                    highest = null;
                else
                    highest = previousHighBids.pop();
                
                System.out.println("*cue sad trombone*");
                return false;
            }
        }
        if(bid.compareTo(highest) <= 0)
            return false;
        
        previousHighBids.push(highest);
        highest = bid;
        return true;
    }
    
    public void endBidding()
    {
        endsAt = Instant.now();
    }
    
    public Bid getHighest()
    {
        return highest;
    }

    public void setHighest(Bid highest)
    {
        this.highest = highest;
    }

    public User getAuctionedBy()
    {
        return auctionedBy;
    }

    public void setAuctionedBy(User auctionedBy)
    {
        this.auctionedBy = auctionedBy;
    }

    public List<Bid> getPreviousHighBids()
    {
        return previousHighBids;
    }
    
    public Instant getStartedAt()
    {
        return startedAt;
    }

    public Instant getEndsAt()
    {
        return endsAt;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        return (obj instanceof Auction) &&
                this.hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.auctionedBy);
        hash = 53 * hash + Objects.hashCode(this.itemName);
        return hash;
    }
}
