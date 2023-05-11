package pl.lodz.p.it.ssbd2023.ssbd01.dto.auth;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

  @NotNull String login;

  @NotNull String password;
}
