package pl.lodz.p.it.ssbd2023.ssbd01.moa.facades;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.OrderMedication;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractFacadeLocal;

import java.util.List;

public interface OrderMedicationFacadeLocal extends AbstractFacadeLocal<OrderMedication> {
    List<OrderMedication> findAll();
}
