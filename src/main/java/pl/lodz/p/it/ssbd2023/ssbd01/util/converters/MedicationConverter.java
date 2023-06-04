package pl.lodz.p.it.ssbd2023.ssbd01.util.converters;

import pl.lodz.p.it.ssbd2023.ssbd01.dto.medication.EditMedicationDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.medication.MedicationDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Medication;

public class MedicationConverter {

  private MedicationConverter() {}

  public static MedicationDTO mapMedicationToMedicationDto(Medication medication) {
    return MedicationDTO.builder()
        .version(medication.getVersion())
        .stock(medication.getStock())
        .name(medication.getName())
        .price(medication.getPrice())
        .categoryId(medication.getCategory().getId())
        .build();
  }

  public static Medication mapMedicationDtoToMedication(MedicationDTO medicationDto) {
    // it is  beyond me why we are using DTOs only in the controllers layer, despite them containing
    // version, which we pass down there manually each time, but i won't argue with this
    return Medication.builder()
        .stock(medicationDto.getStock())
        .name(medicationDto.getName())
        .price(medicationDto.getPrice())
        .build();
  }

  public static Medication mapEditMedicationDtoToMedication(EditMedicationDTO medicationDto) {
    return Medication.builder()
        .name(medicationDto.getName())
        .stock(medicationDto.getStock())
        .price(medicationDto.getPrice())
        .build();
  }
}
