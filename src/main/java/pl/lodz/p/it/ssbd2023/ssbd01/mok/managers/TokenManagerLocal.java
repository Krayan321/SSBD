package pl.lodz.p.it.ssbd2023.ssbd01.mok.managers;


import jakarta.ejb.Local;
import java.util.Date;
import java.util.List;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Token;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.TokenType;

@Local
public interface TokenManagerLocal {

    void sendVerificationToken(Account account, String code);

    void verifyAccount(String token);

    Token findByAccountId(Long id);

    void removeVerificationCode(Token token);

    List<Token> findTokensByTokenTypeAndExpirationDateBefore(TokenType verification, Date halfExpirationDate);
}
