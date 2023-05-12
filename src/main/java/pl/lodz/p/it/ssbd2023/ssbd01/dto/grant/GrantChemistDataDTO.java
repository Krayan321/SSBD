package pl.lodz.p.it.ssbd2023.ssbd01.dto.grant;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GrantChemistDataDTO {

  @NotNull private String licenseNumber;
}
