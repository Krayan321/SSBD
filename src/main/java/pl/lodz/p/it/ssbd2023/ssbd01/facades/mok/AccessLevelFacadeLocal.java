package pl.lodz.p.it.ssbd2023.ssbd01.facades.mok;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.AccessLevel;
import pl.lodz.p.it.ssbd2023.ssbd01.facades.AbstractFacadeLocal;

import java.util.List;

public interface AccessLevelFacadeLocal extends AbstractFacadeLocal<AccessLevel> {
    List<AccessLevel> findAll();
}
