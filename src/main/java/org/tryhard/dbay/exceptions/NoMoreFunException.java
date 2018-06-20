package org.tryhard.dbay.exceptions;

/**
 *
 * @author Daniel Tsukiji
 */
public class NoMoreFunException extends Exception
{
    private static String noVegas =
            "Vegas, baby. VEEEE... What?  Seriously!?  Why not!?" +
            System.lineSeparator();
    public NoMoreFunException(String message)
    {
        super(noVegas + message);
    }
}
