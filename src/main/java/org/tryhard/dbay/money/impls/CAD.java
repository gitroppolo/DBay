package org.tryhard.dbay.money.impls;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Currency;
import org.tryhard.dbay.AuctionController;
import org.tryhard.dbay.exceptions.NoMoreFunException;
import org.tryhard.dbay.exceptions.NoStealingException;
import org.tryhard.dbay.money.interfaces.IWentToVegas;
import org.tryhard.dbay.money.Money;
import org.tryhard.dbay.objects.User;

/**
 *
 * @author Daniel Tsukiji
 */
public class CAD extends Money implements IWentToVegas
{
    private static final MathContext MC =
            new MathContext(2, RoundingMode.HALF_UP);
    private User ownedBy = null;
    
    public CAD()
    {
        currency = Currency.getInstance("CAD");
        amount = new BigDecimal(0.0);
    }
    
    public CAD(BigDecimal amount)
    {
        currency = Currency.getInstance("CAD");
        BigDecimal rounded = amount.round(MC);
        this.amount = rounded;
        AuctionController.takeAPennyLeaveAPenny(
                amount.subtract(rounded), currency);
    }
    
    @Override
    public void setOwnedBy(User ownedBy) throws NoStealingException
    {
        if(this.ownedBy == null)
            this.ownedBy = ownedBy;
        else
        {
            if(ownedBy.equals(User.OG))
                throw new NoStealingException("No thank you, eh!");
            throw new NoStealingException("No thank you, eh!");
        }
    }
    
    @Override
    public void convertTo(Currency currency)
    {
        super.convertTo(currency);
        this.amount = amount.round(MC);
    }
    
    public User getOwnedBy()
    {
        return ownedBy;
    }

    @Override
    public boolean spinTheWheel() throws NoMoreFunException
    {
        return false; //Problems at the border :(
    }
    
    @Override
    public String toString()
    {
        StringBuilder msg = new StringBuilder();
        msg.append("$")
                .append(amount)
                .append(" CAD");
        return msg.toString();
    }
}
