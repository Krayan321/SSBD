package pl.lodz.p.it.ssbd2023.ssbd01.dto;

import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Role;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateAccessLevelDTO {

    private Role role;
}
