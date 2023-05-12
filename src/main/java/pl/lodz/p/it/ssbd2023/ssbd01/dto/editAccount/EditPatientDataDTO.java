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
public class EditPatientDataDTO extends AbstractEntityDTO {

  @Builder
  public EditPatientDataDTO(Long id, Long version, String pesel, String firstName,
                            String lastName, String phoneNumber, String nip) {
    super(id, version);
    this.pesel = pesel;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phoneNumber = phoneNumber;
    this.nip = nip;
  }

  @NotNull
  @Pattern(regexp = "^[0-9]{11}$", message = "Invalid PESEL")
  private String pesel;

  @NotNull private String firstName;

  @NotNull private String lastName;

  @NotNull
  @Pattern(regexp = "^(\\+48)? ?[0-9]{3} ?[0-9]{3} ?[0-9]{3}$", message = "Invalid phone number")
  private String phoneNumber;

  @NotNull
  @Pattern(regexp = "^[0-9]{3}-[0-9]{3}-[0-9]{2}-[0-9]{2}$", message = "Invalid NIP")
  private String nip;
}
