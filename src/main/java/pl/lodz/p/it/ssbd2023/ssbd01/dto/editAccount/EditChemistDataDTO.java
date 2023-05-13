package pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AbstractEntityDTO;

@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditChemistDataDTO extends AbstractEntityDTO {

  @Builder
  public EditChemistDataDTO(Long id, Long version, String licenseNumber) {
    super(id, version);
    this.licenseNumber = licenseNumber;
  }

  @NotNull private String licenseNumber;
}
