package pl.lodz.p.it.ssbd2023.ssbd01.dto.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.common.SignableEntity;

@Getter
@Setter
@NoArgsConstructor
public class EditCategoryDTO implements SignableEntity {

    @NotNull
    private Long version;

    @NotNull
    @Size(max = 50, min = 3)
    private String name;

    @NotNull
    private Boolean isOnPrescription;

    @Override
    public String getSignablePayload() {
        return String.format("%s.%d", name, version);
    }

    @Builder
    public EditCategoryDTO(Long version, String name, Boolean isOnPrescription) {
        this.version = version;
        this.name = name;
        this.isOnPrescription = isOnPrescription;
    }
}
