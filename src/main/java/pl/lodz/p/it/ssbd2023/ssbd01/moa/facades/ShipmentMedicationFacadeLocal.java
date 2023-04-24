package pl.lodz.p.it.ssbd2023.ssbd01.moa.facades;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.ShipmentMedication;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractFacadeLocal;

import java.util.List;

public interface ShipmentMedicationFacadeLocal extends AbstractFacadeLocal<ShipmentMedication> {
    List<ShipmentMedication> findAll();
}
