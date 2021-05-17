package com.safelogisitics.gestionentreprisesusers.exception;

public abstract class ErrorsMessage {

  public static String notFoundMessage(String element) {
    return String.format("%s n'existe pas ou a été supprimer.", element);
  }

  public static String accessDeniedMessage() {
    return String.format("Accès non autorisé pour faire cette action.");
  }
}
