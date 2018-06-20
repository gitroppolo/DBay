package org.tryhard.dbay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 *
 * @author Daniel Tsukiji
 */
@SpringBootApplication
@ComponentScan
public class AuctionDriver
{
    public static void main(String[] args)
    {
        SpringApplication.run(AuctionDriver.class, args);
    }
}
