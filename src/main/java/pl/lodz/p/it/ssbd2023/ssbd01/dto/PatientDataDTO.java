package pl.lodz.p.it.ssbd2023.ssbd01.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Role;

@ToString
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientDataDTO extends AccessLevelDTO {

    @Builder
    public PatientDataDTO(Long id, Long version, Role role, Boolean active, String pesel,
                          String firstName, String lastName, String phoneNumber, String NIP) {
        super(id, version, role, active);
        this.pesel = pesel;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.nip = NIP;
    }

    @NotNull
//    @Pattern(regexp = "^[0-9]{11}$", message = "Invalid PESEL")
    private String pesel;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
//    @Pattern(regexp = "^(\\+48)? ?[0-9]{3} ?[0-9]{3} ?[0-9]{3}$", message = "Invalid phone number")
    private String phoneNumber;

    @NotNull
//    @Pattern(regexp = "^[0-9]{3}-[0-9]{3}-[0-9]{2}-[0-9]{2}$", message = "Invalid NIP")
    private String nip;
}
