package pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditChemistDataDTO extends AbstractEditAccountDTO {

  @Builder
  public EditChemistDataDTO(String login, Long version, String licenseNumber) {
    super(login, version);
    this.licenseNumber = licenseNumber;
  }

  @NotNull
  @Pattern(regexp = "^\\d{6}$", message = "Invalid license number")
  private String licenseNumber;
}
