package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import jakarta.annotation.security.DenyAll;
import jakarta.annotation.security.PermitAll;
import jakarta.ejb.SessionSynchronization;
import jakarta.ejb.Stateful;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.inject.Inject;
import lombok.extern.java.Log;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractManager;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Category;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Medication;
import pl.lodz.p.it.ssbd2023.ssbd01.exceptions.ApplicationException;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.CategoryFacade;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.MedicationFacade;

import java.util.List;

@Stateful
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
@Log
@DenyAll
public class MedicationManager extends AbstractManager implements MedicationManagerLocal, SessionSynchronization {

    @Inject
    private MedicationFacade medicationFacade;

    @Inject
    private CategoryFacade categoryFacade;
    @Override
    @PermitAll
    public Medication createMedication(Medication medication) {
        Long categoryId = medication.getCategory().getId();
        Category managedCategory = categoryFacade.find(categoryId).orElseThrow(ApplicationException::createEntityNotFoundException);
        medication.setCategory(managedCategory);
        medicationFacade.create(medication);
        return medication;
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
    @DenyAll
    public Medication editMedication(Medication medication) {
        throw new UnsupportedOperationException();
    }

    @Override
    @DenyAll
    public Medication getMedicationDetails(Long id) {
        throw new UnsupportedOperationException();
    }
}
