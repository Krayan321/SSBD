package pl.lodz.p.it.ssbd2023.ssbd01.facades.moa;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Order;
import pl.lodz.p.it.ssbd2023.ssbd01.facades.AbstractFacadeLocal;

import jakarta.ejb.Local;
import java.util.List;

@Local
public interface OrderFacadeLocal extends AbstractFacadeLocal<Order> {
    List<Order> findAll();
}
