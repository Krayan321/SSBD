package pl.lodz.p.it.ssbd2023.ssbd01.dto.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NewPasswordDTO {

  @NotNull
  String password;

  @NotNull
  @Size(min = 8, max = 8)
  String token;
}
