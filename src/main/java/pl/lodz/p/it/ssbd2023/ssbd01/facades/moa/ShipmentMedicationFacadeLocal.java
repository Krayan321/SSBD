package pl.lodz.p.it.ssbd2023.ssbd01.facades.moa;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.ShipmentMedication;
import pl.lodz.p.it.ssbd2023.ssbd01.facades.AbstractFacadeLocal;

import java.util.List;

public interface ShipmentMedicationFacadeLocal extends AbstractFacadeLocal<ShipmentMedication> {
    List<ShipmentMedication> findAll();
}
