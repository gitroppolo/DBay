package org.tryhard.dbay.objects;

import java.math.BigDecimal;
import java.util.Objects;
import org.tryhard.dbay.exceptions.NoStealingException;
import org.tryhard.dbay.money.Money;
import org.tryhard.dbay.money.impls.USD;

/**
 *
 * @author Daniel Tsukiji
 */
public class User
{
    public static final User OG =
            new User("Daniel",
                    "Tsukiji",
                    "dannyt@surewest.net",
                    new USD(new BigDecimal(0)));
    
    private String firstName, lastName, email;
    private Money bank;
    private int wonInVegas = 0;

    public User(String email)
    {
        this.email = email;
    }
    
    public User(String firstName, String lastName, String email, Money bank)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.bank = bank;
        try
        {
            this.bank.setOwnedBy(this);
        }
        catch(NoStealingException nse)
        {
            System.err.println("Caught stealing: " + nse.getMessage());
        }
    }
    
    public boolean canGoToVegas()
    {
        return (wonInVegas >= 5);
    }
    
    public void wonInVegas()
    {
        wonInVegas++;
    }
    
    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public String getEmail()
    {
        return email;
    }
    
    @Override
    public boolean equals(Object obj)
    {
        return (obj instanceof User) &&
                this.hashCode() == obj.hashCode();
    }

    @Override
    public int hashCode()
    {
        int hash = 3;
        hash = 71 * hash + Objects.hashCode(this.email);
        return hash;
    }
}
