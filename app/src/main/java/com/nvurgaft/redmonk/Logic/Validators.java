package com.nvurgaft.redmonk.Logic;

/**
 * Created by Koby on 01-Jul-15.
 */
public class Validators {

    /**
     *  Validates a phone number string
     * @param number
     * @return true if valid, otherwise false
     */
    public static boolean validatePhoneNumber(String number) {
        for (int i=0; i<number.length(); i++) {
            if (number.charAt(i)<'1' && number.charAt(i)>'0') {
                return false;
            }
        }
        return true;
    }

    /**
     * Validates a human name, there is nothing special here, the name is valid if it hold only
     * characters without any special symbols or digits.
     *
     * @param name
     * @return true if valid, otherwise false
     */
    public static boolean validateName(String name) {
        String uppercaseString = name.toUpperCase();
        for (int i=0; i<uppercaseString.length(); i++) {
            if (uppercaseString.charAt(i)<'A' && uppercaseString.charAt(i)>'Z') {
                return false;
            }
        }
        return true;
    }
}
