package pl.lodz.p.it.ssbd2023.ssbd01.mok.facades;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.ChemistData;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractFacadeLocal;

import jakarta.ejb.Local;
import java.util.List;

@Local
public interface ChemistDataFacadeLocal extends AbstractFacadeLocal<ChemistData> {
    List<ChemistData> findAll();
}
