package pl.lodz.p.it.ssbd2023.ssbd01.facades.moa;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.OrderMedication;
import pl.lodz.p.it.ssbd2023.ssbd01.facades.AbstractFacadeLocal;

import java.util.List;

public interface OrderMedicationFacadeLocal extends AbstractFacadeLocal<OrderMedication> {
    List<OrderMedication> findAll();
}
