package org.tryhard.dbay.money;

import java.math.BigDecimal;
import java.util.Currency;
import org.tryhard.dbay.exceptions.NoStealingException;
import org.tryhard.dbay.objects.User;

/**
 *
 * @author Daniel Tsukiji
 */
public abstract class Money implements Comparable
{
    private enum Exchange
    {
        USD_CAD(new BigDecimal(1.33)),
        CAD_USD(new BigDecimal(0.75));
        
        private final BigDecimal rate;
        Exchange(BigDecimal rate)
        {
            this.rate = rate;
        }
        
        private BigDecimal getRate()
        {
            return rate;
        }
    }
    
    protected BigDecimal amount;
    protected Currency currency;
    
    public abstract void setOwnedBy(User user) throws NoStealingException;
    @Override
    public abstract String toString();
    
    public BigDecimal getConvertedValue(Currency currency)
    {
        if(this.currency.equals(currency))
            return amount;
        if(this.currency == Currency.getInstance("USD"))
            return amount.multiply(Exchange.USD_CAD.getRate());
        return amount.multiply(Exchange.CAD_USD.getRate());
    }
    
    public void convertTo(Currency currency)
    {
        if(this.currency.equals(currency))
            return;
        if(this.currency == Currency.getInstance("USD"))
        {
            amount = amount.multiply(Exchange.USD_CAD.getRate());
            this.currency = Currency.getInstance("CAD");
        }
        else
        {
            amount = amount.multiply(Exchange.CAD_USD.getRate());
            this.currency = Currency.getInstance("USD");
        }
    }
    
    public void addOnSly(BigDecimal toAdd, Currency currency)
    {
        //How'd I get here!?
        if(currency.equals(this.currency))
            amount.add(toAdd);
        else
        {
            if(currency.getDisplayName().equals("CAD"))
                amount.add(toAdd.multiply(Exchange.USD_CAD.getRate()));
            else if(currency.getDisplayName().equals("USD"))
                amount.add(toAdd.multiply(Exchange.CAD_USD.getRate()));
        }
    }
    
    public void add(Money toAdd)
    {
        toAdd.convertTo(currency);
        
        amount = amount.add(toAdd.amount);
        toAdd.subtract(toAdd);
        //Money can't just be created!
    }
    
    public void subtract(Money toSubtract)
    {
        toSubtract.convertTo(currency);
        
        amount = amount.subtract(toSubtract.amount);
        //You can, however, still owe!
    }
    
    @Override
    public int compareTo(Object o)
    {
        if(!(o instanceof Money))
            return Integer.MIN_VALUE;
        
        return amount.compareTo(((Money)o).getConvertedValue(currency));
    }
}
