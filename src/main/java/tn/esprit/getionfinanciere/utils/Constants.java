package tn.esprit.getionfinanciere.utils;

public class Constants {

  private Constants(){
    throw new IllegalStateException("Utility class");
  }

  public static final String SAISIE_INVALIDE = "Saisie invalide";
  public static final String REGEX_NUMBER = "[1-9][0-9]{7}";
  public static final String REGEX_ADRESS = "[a-zA-Z0-9À-ÿ\\s]+";
  public static final String REGEX_NOM = "[a-zA-ZÀ-ÿ\\s&'%',]+";

}
