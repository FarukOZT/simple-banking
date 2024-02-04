package com.eteration.simplebanking.util;

public class BalanceUtil {

    public static boolean isAmountAvailable(double amount, double accountBalance) {
        return (accountBalance - amount) > 0;
    }
}
