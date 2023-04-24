package pl.lodz.p.it.ssbd2023.ssbd01.moa.facades;

import jakarta.ejb.Local;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.PatientData;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractFacadeLocal;

import java.util.List;

@Local
public interface PatientDataFacadeLocal extends AbstractFacadeLocal<PatientData> {
    List<PatientData> findAll();
}
