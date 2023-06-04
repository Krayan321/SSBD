package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ejb.SessionSynchronization;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import jakarta.interceptor.Interceptors;
import java.util.List;
import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractManager;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Medication;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.GenericManagerExceptionsInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.interceptors.TrackerInterceptor;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.CategoryFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.MedicationFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.mok.managers.AccountManagerLocal;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Log
@Interceptors({GenericManagerExceptionsInterceptor.class, TrackerInterceptor.class})
@DenyAll
public class MedicationManager extends AbstractManager
    implements MedicationManagerLocal, SessionSynchronization {

  @Inject private MedicationFacade medicationFacade;
  @Inject private CategoryFacade categoryFacade;
  @Inject private AccountManagerLocal accountManager;

  @Override
  @DenyAll
  public Medication createMedication(Medication medication) {
    throw new UnsupportedOperationException();
  }

  @Override
  @DenyAll
  public List<Medication> getAllMedications() {
    throw new UnsupportedOperationException();
  }

  @Override
  @DenyAll
  public Medication getMedication(Long id) {
    throw new UnsupportedOperationException();
  }

  @Override
  @RolesAllowed("editMedication")
  public Medication editMedication(
      Long medication_id, Long version, Medication medication, Long category_id) {
    var managed_medication =
        medicationFacade
            .find(medication_id)
            .orElseThrow(ApplicationException::createEntityNotFoundException);
    var new_category =
        categoryFacade
            .find(category_id)
            .orElseThrow(ApplicationException::createEntityNotFoundException);

    if (!managed_medication.getVersion().equals(version)) {
      throw ApplicationException.createOptimisticLockException();
    }

    managed_medication.setStock(medication.getStock());
    managed_medication.setName(medication.getName());
    managed_medication.setPrice(medication.getPrice());
    managed_medication.setCategory(new_category);

    managed_medication.setModifiedBy(accountManager.getCurrentUserLogin());
    medicationFacade.edit(managed_medication);
    return managed_medication;
  }

  @Override
  @DenyAll
  public Medication getMedicationDetails(Long id) {
    throw new UnsupportedOperationException();
  }
}
