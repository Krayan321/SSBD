package pl.lodz.p.it.ssbd2023.ssbd01.mok.managers;

import jakarta.ejb.Local;
import java.util.Date;
import java.util.List;
import pl.lodz.p.it.ssbd2023.ssbd01.common.CommonManagerLocalInterface;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Token;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.TokenType;

@Local
public interface TokenManagerLocal extends CommonManagerLocalInterface {

  void sendVerificationToken(Account account, String code);

  void sendResetPasswordToken(Account account);

  void verifyAccount(String token);

  Token findByAccountId(Long id);

  void removeVerificationCode(Token token);

  List<Token> findTokensByTokenTypeAndExpirationDateBefore(
      TokenType verification, Date halfExpirationDate);
}
