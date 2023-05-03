package pl.lodz.p.it.ssbd2023.ssbd01.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterPatientDto {

    @NotNull
    private String login;

    @NotNull
    private String password;

    @NotNull
//    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    private String email;

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

}
