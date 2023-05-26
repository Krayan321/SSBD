package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import jakarta.annotation.security.DenyAll;
import jakarta.ejb.SessionSynchronization;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractManager;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Shipment;

import java.util.List;

public class ShipmentManager extends AbstractManager implements ShipmentManagerLocal, SessionSynchronization {
    @Override
    @DenyAll
    public Shipment createShipment(Shipment shipment) {
        throw new UnsupportedOperationException();
    }

    @Override
    @DenyAll
    public Shipment getShipment(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    @DenyAll
    public Shipment editShipment(Shipment shipment) {
        throw new UnsupportedOperationException();
    }

    @Override
    @DenyAll
    public List<Shipment> getAllShipments() {
        throw new UnsupportedOperationException();
    }
}
