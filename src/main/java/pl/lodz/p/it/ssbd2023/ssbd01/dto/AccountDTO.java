package pl.lodz.p.it.ssbd2023.ssbd01.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public class AccountDTO extends AbstractEntityDTO {

  @Builder
  public AccountDTO(
      Long id, Long version, String login, Boolean active, Boolean confirmed, String email) {
    super(id, version);
    this.login = login;
    this.active = active;
    this.confirmed = confirmed;
    this.email = email;
  }

  @NotNull private String login;

  @NotNull private String email;

  @NotNull private Boolean active;

  @NotNull private Boolean confirmed;
}
