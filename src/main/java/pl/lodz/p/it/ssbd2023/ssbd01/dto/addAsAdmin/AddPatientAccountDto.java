package pl.lodz.p.it.ssbd2023.ssbd01.dto.addAsAdmin;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.register.BasicAccountDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddPatientAccountDto extends BasicAccountDto {

  @NotNull
  @Size(max = 50, min = 2)
  private String name;

  @NotNull
  @Size(max = 50, min = 2)
  private String lastName;

  @NotNull
  @Pattern(regexp = "^\\d{9}$", message = "Invalid phone number")
  private String phoneNumber;

  @NotNull
  @Pattern(regexp = "^\\d{11}$", message = "Invalid PESEL")
  private String pesel;

  @NotNull
  @Pattern(regexp = "^\\d{10}$", message = "Invalid NIP")
  private String NIP;

  @NotNull
  private boolean confirmed;

  @NotNull
  private boolean active;
}
