package pl.lodz.p.it.ssbd2023.ssbd01.dto.register;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BasicAccountDto {

  @NotNull private String login;

  @NotNull private String password;

  @NotNull
  //    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
  private String email;

  private String language;
}
