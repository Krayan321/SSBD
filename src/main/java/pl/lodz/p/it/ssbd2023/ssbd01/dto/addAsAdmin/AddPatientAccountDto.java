package pl.lodz.p.it.ssbd2023.ssbd01.dto.addAsAdmin;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.register.BasicAccountDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddPatientAccountDto extends BasicAccountDto {

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
    private String NIP;

    private boolean confirmed;

    private boolean active;

}
