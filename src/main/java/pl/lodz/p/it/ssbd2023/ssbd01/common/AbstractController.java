package pl.lodz.p.it.ssbd2023.ssbd01.common;

import jakarta.ejb.EJBTransactionRolledbackException;
import jakarta.inject.Inject;
import java.util.function.Supplier;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.TransactionException;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationExceptionOptimisticLock;

public class AbstractController {

    @Inject
    @ConfigProperty(name = "transaction.repeat.count")
    private int TRANSACTION_REPEAT_COUNT;

    protected <T> T repeatTransaction(CommonManagerLocalInterface service, Supplier<T> method) {
        boolean rollbackTX = false;

        T result = null;
        do {
            try {
                result = method.get();
                rollbackTX = service.isLastTransactionRollback();
            } catch (EJBTransactionRolledbackException e) {
                rollbackTX = true;
            }
        } while (rollbackTX && --TRANSACTION_REPEAT_COUNT > 0);

        if (rollbackTX && TRANSACTION_REPEAT_COUNT == 0) {
            throw new TransactionException("Transaction failed");
        }
        return result;
    }

    protected void repeatTransactionVoid(CommonManagerLocalInterface service, VoidFI voidFI) {
        int retryTXCounter = TRANSACTION_REPEAT_COUNT;
        boolean rollbackTX = false;

        do {
            try {
                voidFI.action();
                rollbackTX = service.isLastTransactionRollback();
            } catch (EJBTransactionRolledbackException | ApplicationExceptionOptimisticLock e) {
                rollbackTX = true;
            }
        } while (rollbackTX && --retryTXCounter > 0);

        if (rollbackTX && retryTXCounter == 0) {
            throw new TransactionException("Transaction failed");
        }
    }

    @FunctionalInterface
    public interface VoidFI {
        void action();
    }

}
