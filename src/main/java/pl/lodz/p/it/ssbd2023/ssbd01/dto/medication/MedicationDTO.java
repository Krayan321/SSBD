package pl.lodz.p.it.ssbd2023.ssbd01.dto.medication;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class MedicationDTO {

  @NotNull
  @Size(max = 50, min = 2)
  private String name;

  @Digits(integer = 10, fraction = 2)
  @Min(value = 0, message = "Price must be greater than or equal 0")
  @NotNull private BigDecimal price;

  @NotNull private CategoryDTO category;
}
