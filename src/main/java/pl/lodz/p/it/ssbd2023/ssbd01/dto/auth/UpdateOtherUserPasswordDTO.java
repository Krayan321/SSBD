package pl.lodz.p.it.ssbd2023.ssbd01.dto.auth;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AbstractEditEntityDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOtherUserPasswordDTO extends AbstractEditEntityDTO {

  @NotNull
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$", message = "Invalid password")
  @Size(max = 50, min = 8)
  String password;
}
