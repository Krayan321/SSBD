package pl.lodz.p.it.ssbd2023.ssbd01.common;

import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;

public interface AbstractFacadeLocal<T> {
    void create(T entity);
    T find(Object id);
    void edit(T entity);
    void remove(T entity);
}
