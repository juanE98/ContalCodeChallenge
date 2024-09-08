package net.contal.demo;

import java.util.Random;

public abstract class AccountNumberUtil {


    /**
     * implement this function
     * this function should generate random integer number and return
     * @return random integer
     */
    public static int generateAccountNumber(){
        Random random = new Random();
        return 1000000000 + random.nextInt(900000000);
    }

}
