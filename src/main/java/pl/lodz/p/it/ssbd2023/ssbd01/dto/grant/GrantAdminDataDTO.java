package pl.lodz.p.it.ssbd2023.ssbd01.dto.grant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.common.SignableEntity;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GrantAdminDataDTO implements SignableEntity {

  @NotNull
  @Pattern(regexp = "^\\d{9}$", message = "Invalid phone number")
  private String workPhoneNumber;

  @Override
  public String getSignablePayload() {
    return workPhoneNumber;
  }
}
