package org.tryhard.dbay.objects;

import java.math.BigDecimal;
import org.tryhard.dbay.money.Money;

/**
 *
 * @author Daniel Tsukiji
 */
public class Bid implements Comparable
{
    private Money money;
    private User bidder;

    public Bid(Money money, User bidder)
    {
        this.money = money;
        this.bidder = bidder;
    }
    
    public Money getMoney()
    {
        return money;
    }

    public void setMoney(Money money)
    {
        this.money = money;
    }

    public User getBidder()
    {
        return bidder;
    }

    public void setBidder(User bidder)
    {
        this.bidder = bidder;
    }

    @Override
    public int compareTo(Object o)
    {
        if(!(o instanceof Bid))
            return Integer.MIN_VALUE;
        return money.compareTo(((Bid)o).money);
    }
    
    @Override
    public String toString()
    {
        StringBuilder msg = new StringBuilder();
        msg.append(bidder.getLastName())
                .append(", ")
                .append(bidder.getFirstName())
                .append(": ")
                .append(money);
        return msg.toString();
    }
}
