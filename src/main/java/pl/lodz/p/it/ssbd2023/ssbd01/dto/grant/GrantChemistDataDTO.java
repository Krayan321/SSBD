package pl.lodz.p.it.ssbd2023.ssbd01.dto.grant;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.lodz.p.it.ssbd2023.ssbd01.common.SignableEntity;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GrantChemistDataDTO implements SignableEntity {
  @NotNull
  @Pattern(regexp = "^\\d{6}$", message = "Invalid license number")
  private String licenseNumber;

  @Override
  public String getSignablePayload() {
    return licenseNumber;
  }
}
