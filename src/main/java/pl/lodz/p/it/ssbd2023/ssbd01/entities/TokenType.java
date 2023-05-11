package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import lombok.Getter;

@Getter
public enum TokenType {
  VERIFICATION("VERIFICATION", "Weryfikacja konta"),
  PASSWORD_RESET("PASSWORD_RESET", "Resetowanie hasła");

  private String tokenName;

  private String tokenDescription;

  TokenType(String tokenName, String tokenDescription) {
    this.tokenName = tokenName;
    this.tokenDescription = tokenDescription;
  }
}
