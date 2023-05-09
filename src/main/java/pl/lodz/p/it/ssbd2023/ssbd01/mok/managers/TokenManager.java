package pl.lodz.p.it.ssbd2023.ssbd01.mok.managers;

import static pl.lodz.p.it.ssbd2023.ssbd01.exceptions.TokenException.incorrectTokenTypeException;
import static pl.lodz.p.it.ssbd2023.ssbd01.exceptions.TokenException.tokenAlreadyUsedException;
import static pl.lodz.p.it.ssbd2023.ssbd01.exceptions.TokenException.tokenExpiredException;

import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import lombok.extern.java.Log;
import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Token;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.TokenType;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.TokenException;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.GenericManagerExceptionsInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.mok.facades.TokenFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.util.email.EmailService;

@Stateful
@Log
@TransactionAttribute(TransactionAttributeType.MANDATORY)
@Interceptors({GenericManagerExceptionsInterceptor.class})
public class TokenManager implements TokenManagerLocal {

    @Inject
    TokenFacade tokenFacade;

    @Inject
    EmailService emailService;

    @Inject
    @ConfigProperty(name = "verification.token.expiration.hours")
    private int VERIFICATION_TOKEN_EXPIRATION_HOURS;

    @Override
    public void sendVerificationToken(Account account, String code) {
        Token token = Token.builder()
                .account(account)
                .code(code == null ? RandomStringUtils.randomAlphanumeric(8) : code)
                .tokenType(TokenType.VERIFICATION)
                .wasPreviousTokenSent(false)
                .expirationDate(new Date(
                        Instant.now().plusSeconds(
                                (long) VERIFICATION_TOKEN_EXPIRATION_HOURS * 60 * 60)
                                .toEpochMilli()))
                .build();
        //todo czy napewno createdby/modifiedby przez account?
        token.setCreatedBy(account);
        token.setModifiedBy(account);

        tokenFacade.create(token);
        emailService.sendRegistrationEmail(
                account.getEmail(),
                account.getLogin(),
                token.getCode()
        );
    }

    @Override
    public void verifyAccount(String code) {
        Token token = tokenFacade.findByCode(code);
        checkIfTokenIsValid(token);
        token.getAccount().setConfirmed(true);
        token.getAccount().setCreatedBy(token.getAccount());
        token.getAccount().setModifiedBy(token.getAccount());
        token.setUsed(true);
        tokenFacade.edit(token);
    }

    private void checkIfTokenIsValid(Token token) {
        if (token == null) {
            TokenException.tokenNotFoundException();
        }
        Account account = token.getAccount();

        if (token.getExpirationDate().before(new java.util.Date())) {
            tokenExpiredException();
        }
        if (token.isUsed() || account.getConfirmed()) {
            log.warning("MAGNO MANGO ZET Token already used");
            tokenAlreadyUsedException();
        }
        if (token.getTokenType() != TokenType.VERIFICATION) {
            incorrectTokenTypeException();
        }
    }

    @Override
    public Token findByAccountId(Long id) {
        return tokenFacade.findByAccount(id);
    }

    @Override
    public void removeVerificationCode(Token token) {
        tokenFacade.remove(token);
    }

    @Override
    public List<Token> findTokensByTokenTypeAndExpirationDateBefore(TokenType verification,
                                                                    Date halfExpirationDate) {
        return tokenFacade.findByTypeAndBeforeGivenData(verification,
                halfExpirationDate);
    }

}
