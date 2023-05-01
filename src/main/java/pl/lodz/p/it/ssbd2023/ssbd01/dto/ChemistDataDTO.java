package pl.lodz.p.it.ssbd2023.ssbd01.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Account;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Role;

@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChemistDataDTO extends AccessLevelDTO {

    @Builder
    public ChemistDataDTO(Long id, Long version, Role role, Boolean active, Account account, String licenseNumber) {
        super(id, version, role, active, account);
        this.licenseNumber = licenseNumber;
    }

    @NotNull
    private String licenseNumber;
}
