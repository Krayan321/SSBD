package pl.lodz.p.it.ssbd2023.ssbd01.dto.addAsAdmin;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.register.BasicAccountDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddAdminAccountDto extends BasicAccountDto {

    @NotNull
    //    @Pattern(regexp = "^(\\+48)? ?[0-9]{3} ?[0-9]{3} ?[0-9]{3}$", message = "Invalid phone number")
    private String workPhoneNumber;

    @Builder
    public AddAdminAccountDto(@NotNull String login,
                              @NotNull String password,
                              @NotNull String email, String language, String workPhoneNumber) {
        super(login, password, email, language);
        this.workPhoneNumber = workPhoneNumber;
    }
}
