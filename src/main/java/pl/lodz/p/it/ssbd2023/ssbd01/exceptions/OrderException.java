package pl.lodz.p.it.ssbd2023.ssbd01.exceptions;

import jakarta.ws.rs.core.Response;

import static jakarta.ws.rs.core.Response.Status.*;
import static pl.lodz.p.it.ssbd2023.ssbd01.common.i18n.*;

@jakarta.ejb.ApplicationException(rollback = true)
public class OrderException extends ApplicationException {

    private OrderException(Response.Status status, String key) {
        super(status, key);
    }

    private OrderException(Response.Status status, String key, Exception e) {
        super(status, key, e);
    }

    public static OrderException onlyPatientCanOrder() {
        return new OrderException(FORBIDDEN, EXCEPTION_ORDER_ONLY_PATIENT_CAN_ORDER);
    }

}
