package pl.lodz.p.it.ssbd2023.ssbd01.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd01.common.SignableEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditAccountDTO implements SignableEntity {

  @NotNull
  @Email
  @Size(max = 50, min = 5)
  String email;

  @Override
  public String getSignablePayload() {
    return email + "." + "1";
  }
}
