package com.safelogisitics.gestionentreprisesusers.util;

import com.safelogisitics.gestionentreprisesusers.data.enums.ECompteType;

import java.time.LocalDate;
import java.util.Random;

public class ClientNumberGeneratorUtils {

  private static final String S_S = "%s%s%s";
  
  public static String generateReference(String numeroCarte, ECompteType compteType){

    String registrationNumber = "";
    String initial = "";

    int month = LocalDate.now().getMonthValue();
    String monthStr = month < 10 ? "0"+month : String.valueOf(month);

    if (numeroCarte == null) {
      int m = (int) Math.pow(10, 2);
      int randomInt = m + new Random().nextInt(9 * m);

      switch(compteType) {
        case COMPTE_PARTICULIER:
          initial = "300";
          break;
        case COMPTE_ADMINISTRATEUR:
          initial = "100";
          break;
        case COMPTE_COURSIER:
          initial = "400";
          break;
        case COMPTE_PRESTATAIRE:
          initial = "500";
          break;
        default:
          initial = "XXX";
      }

      registrationNumber = String.format(S_S, initial, monthStr, randomInt);
    }
    else {
      registrationNumber = String.format(S_S, "600", monthStr, numeroCarte.substring(numeroCarte.length() - 4));
    }

    return registrationNumber;
  }
}
