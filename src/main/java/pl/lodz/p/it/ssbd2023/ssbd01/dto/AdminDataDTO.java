package pl.lodz.p.it.ssbd2023.ssbd01.dto;

import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Role;

@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
public class AdminDataDTO extends AccessLevelDTO {

    @Builder
    public AdminDataDTO(Long id, Long version, Role role, Boolean active) {
        super(id, version, role, active);
    }
}
