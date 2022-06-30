package com.jovinn.capstoneproject.util;

import net.bytebuddy.utility.RandomString;

import java.util.Random;

public class GenerateRandomNumber {
    public static String getRandomNumberString() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    public static String getRandomContractNumber() {
        String code = RandomString.make(8);
        return "JOV-" + code.toUpperCase();
    }
}
