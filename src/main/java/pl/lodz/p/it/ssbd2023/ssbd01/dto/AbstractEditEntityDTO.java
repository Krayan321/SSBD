package pl.lodz.p.it.ssbd2023.ssbd01.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.common.SignableEntity;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class AbstractEditEntityDTO implements SignableEntity {

    @NotNull
    private Long version;

    @Override
    public String getSignablePayload() {
        return version.toString();
    }
}
