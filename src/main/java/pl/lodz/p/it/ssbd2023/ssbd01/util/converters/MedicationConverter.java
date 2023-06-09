package pl.lodz.p.it.ssbd2023.ssbd01.util.converters;

import pl.lodz.p.it.ssbd2023.ssbd01.dto.medication.MedicationDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Medication;

public class MedicationConverter {

  private MedicationConverter() {}

  public static MedicationDTO mapMedicationToMedicationDTO(Medication medication) {
    return MedicationDTO.builder()
        .category(CategoryConverter.mapCategoryToCategoryDTO(medication.getCategory()))
        .name(medication.getName())
        .currentPrice(medication.getCurrentPrice())
        .build();
  }
}
