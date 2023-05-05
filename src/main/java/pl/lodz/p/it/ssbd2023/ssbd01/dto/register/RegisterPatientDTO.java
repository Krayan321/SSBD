package pl.lodz.p.it.ssbd2023.ssbd01.dto.register;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterPatientDTO extends BasicAccountDto {

    @NotNull
    private String name;

    @NotNull
    private String lastName;

    @NotNull
//    @Pattern(regexp = "^(\\+48)? ?[0-9]{3} ?[0-9]{3} ?[0-9]{3}$", message = "Invalid phone number")
    private String phoneNumber;

    @NotNull
//    @Pattern(regexp = "^[0-9]{11}$", message = "Invalid PESEL")
    private String pesel;

    @NotNull
//    @Pattern(regexp = "^[0-9]{3}-[0-9]{3}-[0-9]{2}-[0-9]{2}$", message = "Invalid NIP")
    private String nip;

    @Builder
    public RegisterPatientDTO(@NotNull String login,
                              @NotNull String password,
                              @NotNull String email, String language, String name,
                              String lastName, String phoneNumber, String pesel, String nip) {
        super(login, password, email, language);
        this.name = name;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.pesel = pesel;
        this.nip = nip;
    }
}
