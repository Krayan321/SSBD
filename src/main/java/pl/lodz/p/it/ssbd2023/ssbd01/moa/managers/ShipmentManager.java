package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionSynchronization;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractManager;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Shipment;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.ShipmentMedication;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.GenericManagerExceptionsInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.MedicationFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.ShipmentFacade;

import java.util.List;
import java.util.Optional;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Interceptors({GenericManagerExceptionsInterceptor.class,
        TrackerInterceptor.class})
@Log
@DenyAll
public class ShipmentManager extends AbstractManager implements ShipmentManagerLocal, SessionSynchronization {

    @Inject
    private ShipmentFacade shipmentFacade;

    @Inject
    private MedicationFacade medicationFacade;


    @Override
    @RolesAllowed("createShipment")
    public void createShipment(Shipment shipment) {
        shipment.getShipmentMedications().forEach(sm -> {
                    sm.setShipment(shipment);
                    sm.setMedication(medicationFacade.find(
                            sm.getMedication().getId()).orElseThrow());
                });
        shipmentFacade.create(shipment);
    }

    @Override
    @RolesAllowed("readAllShipments")
    public List<Shipment> getAllShipments() {
        return shipmentFacade.findAll();
    }

    @Override
    @RolesAllowed("readShipment")
    public Shipment getShipment(Long id) {
        Optional<Shipment> shipmentOpt = shipmentFacade.find(id);
        if (shipmentOpt.isEmpty()) {
            throw ApplicationException.createEntityNotFoundException();
        }
        return shipmentOpt.get();
    }
}
