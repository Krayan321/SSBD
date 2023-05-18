package pl.lodz.p.it.ssbd2023.ssbd01.dto.editAccessLevel;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd01.common.SignableEntity;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Role;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AbstractEditAccessLevelDTO implements SignableEntity {

    @NotNull
    private Long version;

    @NotNull
    private Role role;

    @Override
    public String getSignablePayload() {
        return String.format("%s.%d", role, version);
    }
}
