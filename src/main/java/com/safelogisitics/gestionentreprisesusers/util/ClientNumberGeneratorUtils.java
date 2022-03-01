package com.safelogisitics.gestionentreprisesusers.util;

import java.time.LocalDate;
import java.util.Random;

public class ClientNumberGeneratorUtils {

  private static final String S_S = "%s%s%s";
  
  public static String generateNumber(String initial, int length, String numeroCarte) {
    String registrationNumber = "";

    int month = LocalDate.now().getMonthValue();
    String monthStr = month < 10 ? "0"+month : String.valueOf(month);

    if (numeroCarte == null) {
      int m = (int) Math.pow(10, length - 1);
      int randomInt = m + new Random().nextInt(9 * m);
      registrationNumber = String.format(S_S, initial, monthStr, randomInt);
    }
    else {
      registrationNumber = String.format(S_S, initial, monthStr, numeroCarte.substring(numeroCarte.length() - length));
    }

    return registrationNumber;
  }
}
