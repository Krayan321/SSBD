package pl.lodz.p.it.ssbd2023.ssbd01.facades.moa;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.Category;
import pl.lodz.p.it.ssbd2023.ssbd01.facades.AbstractFacadeLocal;

import jakarta.ejb.Local;
import java.util.List;

@Local
public interface CategoryFacadeLocal extends AbstractFacadeLocal<Category> {
    List<Category> findAll();
}
