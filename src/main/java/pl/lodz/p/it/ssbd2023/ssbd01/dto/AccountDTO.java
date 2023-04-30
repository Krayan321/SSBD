package pl.lodz.p.it.ssbd2023.ssbd01.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO extends AbstractEntityDTO {

    Set<AccessLevelDTO> accessLevels;

    @NotNull
    private String login;

    @NotNull
    private String email;

    @NotNull
    private Boolean active;

    @NotNull
    private Boolean confirmed;
}
