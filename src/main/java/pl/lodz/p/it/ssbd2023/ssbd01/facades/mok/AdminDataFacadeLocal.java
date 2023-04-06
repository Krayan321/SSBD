package pl.lodz.p.it.ssbd2023.ssbd01.facades.mok;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.AdminData;
import pl.lodz.p.it.ssbd2023.ssbd01.facades.AbstractFacadeLocal;

import javax.ejb.Local;
import java.util.List;

@Local
public interface AdminDataFacadeLocal extends AbstractFacadeLocal<AdminData> {
    List<AdminData> findAll();
}
