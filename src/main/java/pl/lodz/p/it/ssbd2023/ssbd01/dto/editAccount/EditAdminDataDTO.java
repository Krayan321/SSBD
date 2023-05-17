package pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccount;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AbstractEditEntityDTO;

@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EditAdminDataDTO extends AbstractEditEntityDTO {

  @Builder
  public EditAdminDataDTO(String login, Long version, String workPhoneNumber) {
    super(login, version);
    this.workPhoneNumber = workPhoneNumber;
  }

  @NotNull
  @Pattern(regexp = "^\\d{9}$", message = "Invalid phone number")
  private String workPhoneNumber;
}
