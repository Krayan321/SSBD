package pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AbstractEntityDTO;

@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditAdminDataDTO extends AbstractEntityDTO {

  @Builder
  public EditAdminDataDTO(Long id, Long version, String workPhoneNumber) {
    super(id, version);
    this.workPhoneNumber = workPhoneNumber;
  }

  @NotNull
  @Pattern(regexp = "^(\\+48)? ?[0-9]{3} ?[0-9]{3} ?[0-9]{3}$", message = "Invalid phone number")
  private String workPhoneNumber;
}
