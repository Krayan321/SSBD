package pl.lodz.p.it.ssbd2023.ssbd01.moa.managers;

import java.util.List;
import pl.lodz.p.it.ssbd2023.ssbd01.common.CommonManagerLocalInterface;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Medication;

public interface MedicationManagerLocal extends CommonManagerLocalInterface {

  Medication createMedication(Medication medication);

  List<Medication> getAllMedications();

  Medication getMedication(Long id);

  Medication editMedication(
      Long medication_id, Long version, Medication medication, Long category_id);

  Medication getMedicationDetails(Long id);
}
