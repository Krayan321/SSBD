package pl.lodz.p.it.ssbd2023.ssbd01.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Role;

@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@NoArgsConstructor
public abstract class AccessLevelDTO extends AbstractEntityDTO {

  public AccessLevelDTO(Long id, Long version, Role role, Boolean active) {
    super(id, version);
    this.role = role;
    this.active = active;
  }

  private Role role;

  @NotNull private Boolean active;
}
