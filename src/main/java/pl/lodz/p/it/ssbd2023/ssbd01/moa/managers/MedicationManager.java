package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import jakarta.annotation.security.DenyAll;
import jakarta.ejb.SessionSynchronization;
import jakarta.inject.Inject;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractManager;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Medication;
import pl.lodz.p.it.ssbd2023.ssbd01.moa.facades.MedicationFacade;

import java.util.List;

public class MedicationManager extends AbstractManager implements MedicationManagerLocal, SessionSynchronization {

    @Inject
    private MedicationFacade medicationFacade;
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
