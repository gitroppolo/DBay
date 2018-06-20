package org.tryhard.dbay.money;

import org.tryhard.dbay.money.impls.*;
import java.math.BigDecimal;
import org.tryhard.dbay.exceptions.UnknownCurrencyException;

/**
 *
 * @author Daniel Tsukiji
 */
public class MoneyUtil
{
    public static Money makeMoney(double amount, String currency) throws UnknownCurrencyException
    {
        Money money;
        switch(currency.toUpperCase())
        {
            case "USD":
                money = new USD(new BigDecimal(amount));
                break;
            case "CAD":
                money = new CAD(new BigDecimal(amount));
                break;
            default:
                throw new UnknownCurrencyException("This exchange does not deal in " + currency);
        }
        return money;
    }
}
