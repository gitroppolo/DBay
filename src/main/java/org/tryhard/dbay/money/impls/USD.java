package org.tryhard.dbay.money.impls;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Currency;
import org.tryhard.dbay.DBayConstants;
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
public class USD extends Money implements IWentToVegas
{
    private static final MathContext MC =
            new MathContext(2, RoundingMode.HALF_UP);
    private User ownedBy = null;
    
    public USD()
    {
        currency = Currency.getInstance("USD");
        amount = new BigDecimal(0.0);
    }
    
    public USD(BigDecimal amount)
    {
        currency = Currency.getInstance("USD");
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
                throw new NoStealingException("Catch me outside!");
            throw new NoStealingException(
                    "Ah ah ah... you didn't say the magic word.");
        }
    }
    
    @Override
    public void convertTo(Currency currency)
    {
        super.convertTo(currency);
        this.amount = amount.round(MC);
        /*Tempted to takeAPennyLeaveAPenny here but I don't want to risk
        * getting caught in an infinite loop of conversions!
        */
    }
    
    public User getOwnedBy()
    {
        return ownedBy;
    }
    
    private void doubleIt()
    {
        amount.multiply(new BigDecimal(2));
    }

    @Override
    public boolean spinTheWheel() throws NoMoreFunException
    {
        if(!ownedBy.canGoToVegas())
            throw new NoMoreFunException("I think you've won enough...");
        
        if(DBayConstants.RNG.nextDouble() <= .25)
        {
            doubleIt();
            return true;
        }
        amount = new BigDecimal(0.0);
        return false;
    }
    
    @Override
    public String toString()
    {
        StringBuilder msg = new StringBuilder();
        msg.append("$")
                .append(amount)
                .append(" USD");
        return msg.toString();
    }
}
