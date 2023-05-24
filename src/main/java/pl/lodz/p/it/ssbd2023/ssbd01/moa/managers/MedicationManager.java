package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import jakarta.ejb.SessionSynchronization;
import pl.lodz.p.it.ssbd2023.ssbd01.common.AbstractManager;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Medication;

import java.util.List;

public class MedicationManager extends AbstractManager implements MedicationManagerLocal, SessionSynchronization {
    @Override
    public Medication createMedication(Medication medication) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Medication> getAllMedications() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Medication getMedication(Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Medication editMedication(Medication medication) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Medication getMedicationDetails(Long id) {
        throw new UnsupportedOperationException();
    }
}
