package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionSynchronization;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractManager;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Shipment;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.ShipmentMedication;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.MedicationFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.ShipmentFacade;

import java.util.List;
import java.util.Optional;

public class ShipmentManager extends AbstractManager implements ShipmentManagerLocal, SessionSynchronization {

    @Inject
    private ShipmentFacade shipmentFacade;

    @Inject
    private MedicationFacade medicationFacade;


    @Override
    @RolesAllowed("createShipment")
    public void createShipment(Shipment shipment) {

        for(ShipmentMedication shipmentMedication : shipment.getShipmentMedications()) {

        }
        shipmentFacade.create(shipment);
        // todo mechanizm przeliczania kolejki
    }

    @Override
    @RolesAllowed("readAllShipments")
    public List<Shipment> getAllShipments() {
        return shipmentFacade.findAll();
    }

    @Override
    @RolesAllowed({"readShipment", "updateShipment"})
    public Shipment getShipment(Long id) {
        Optional<Shipment> shipmentOpt = shipmentFacade.find(id);
        if (shipmentOpt.isEmpty()) {
            throw ApplicationException.createEntityNotFoundException();
        }
        return shipmentOpt.get();
    }
}
