package pl.lodz.p.it.ssbd2023.ssbd01.common;

import jakarta.annotation.Resource;
import jakarta.annotation.security.PermitAll;
import jakarta.ejb.SessionContext;
import jakarta.ejb.TransactionAttribute;

import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Logger;
import java.util.logging.Level;

import static jakarta.ejb.TransactionAttributeType.NOT_SUPPORTED;

public abstract class AbstractManager {

    private static final long serialVersionUID = 1L;

    @Resource
    SessionContext sessionContext;

    protected static final Logger LOGGER = Logger.getGlobal();

    private String transactionId;

    private boolean lastTransactionCommited;


    @PermitAll
    @TransactionAttribute(NOT_SUPPORTED)
    public boolean isLastTransactionCommited() {
        return lastTransactionCommited;
    }
    public void afterBegin() {
        transactionId = Long.toString(System.currentTimeMillis())
                + ThreadLocalRandom.current().nextLong(Long.MAX_VALUE);
        LOGGER.log(Level.INFO,
                "Transakcja o id={0}, rozpoczęta w: {1}; tożsamość: {2}",
                new Object[]{
                        transactionId,
                        this.getClass().getName(),
                        sessionContext.getCallerPrincipal().getName()
                }
        );
    }

    public void beforeCompletion() {
        LOGGER.log(
                Level.INFO,
                "Transakcja o id={0}, przed zatwierdzeniem w {1}; tożsamość: {2}",
                new Object[]{
                        transactionId,
                        this.getClass().getName(),
                        sessionContext.getCallerPrincipal().getName()
                }
        );
    }

    public void afterCompletion(boolean committed) {
        lastTransactionCommited = committed;
        LOGGER.log(
                Level.INFO,
                "Transakcja o id={0}, zakończona w {1} (status: {2}); tożsamość {3}",
                new Object[]{
                        transactionId,
                        this.getClass().getName(),
                        committed ? "ZATWIERDZONO" : "ODWOŁANO",
                        sessionContext.getCallerPrincipal().getName()
                }
        );
    }
}
