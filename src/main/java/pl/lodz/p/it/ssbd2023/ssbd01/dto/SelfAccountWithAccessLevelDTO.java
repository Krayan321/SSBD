package pl.lodz.p.it.ssbd2023.ssbd01.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @Size(max = 50, min = 5)
    @Email
    private String email;

    private String language;
}
