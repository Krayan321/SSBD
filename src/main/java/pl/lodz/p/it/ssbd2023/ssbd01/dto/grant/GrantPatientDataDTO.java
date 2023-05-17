package pl.lodz.p.it.ssbd2023.ssbd01.dto.grant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.common.SignableEntity;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GrantPatientDataDTO implements SignableEntity {

  @NotNull
  @Pattern(regexp = "^\\d{11}$", message = "Invalid PESEL")
  private String pesel;

  @NotNull
  @Size(max = 50, min = 2)
  private String firstName;

  @NotNull
  @Size(max = 50, min = 2)
  private String lastName;

  @NotNull
  @Pattern(regexp = "^\\d{9}$", message = "Invalid phone number")
  private String phoneNumber;

  @NotNull
  @Pattern(regexp = "^\\d{10}$", message = "Invalid NIP")
  private String nip;

  @Override
  public String getSignablePayload() {
    return pesel + firstName + lastName + phoneNumber + nip;
  }
}
