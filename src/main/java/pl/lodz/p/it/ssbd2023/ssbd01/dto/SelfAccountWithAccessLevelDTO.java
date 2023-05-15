package pl.lodz.p.it.ssbd2023.ssbd01.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Locale;
import java.util.Set;
@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SelfAccountWithAccessLevelDTO extends AccountAndAccessLevelsDTO {
    @Builder(builderMethodName = "childBuilder")
    public SelfAccountWithAccessLevelDTO(
            Long id,
            Long version,
            Set<AccessLevelDTO> accessLevels,
            String login,
            Boolean active,
            Boolean confirmed,
            String email,
            Locale language) {
        super(id, version, accessLevels, login, active, confirmed);
        this.language = language.getLanguage();
        this.email = email;
    }
    @NotNull
    private String email;

    @NotNull
    private String language;
}
