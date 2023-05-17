package pl.lodz.p.it.ssbd2023.ssbd01.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import java.util.Set;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountAndAccessLevelsDTO extends AbstractEntityDTO {

  @Builder
  public AccountAndAccessLevelsDTO(
      Long id,
      Long version,
      Set<AccessLevelDTO> accessLevels,
      String login,
      String email,
      Boolean active,
      Boolean confirmed) {
    super(id, version);
    this.accessLevels = accessLevels;
    this.login = login;
    this.active = active;
    this.confirmed = confirmed;
    this.email = email;
  }

  @ToString.Exclude Set<AccessLevelDTO> accessLevels;

  @Size(max = 50, min = 5)
  @NotNull
  private String login;

  @Email
  @Size(max = 50, min = 5)
  @NotNull
  private String email;

  @NotNull
  private Boolean active;

  @NotNull
  private Boolean confirmed;
}
