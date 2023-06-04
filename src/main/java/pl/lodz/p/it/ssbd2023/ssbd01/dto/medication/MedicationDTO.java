package pl.lodz.p.it.ssbd2023.ssbd01.dto.medication;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.*;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MedicationDTO {

  @NotNull private Long version;

  @NotNull private Integer stock;

  @NotNull private String name;

  @NotNull private BigDecimal price;

  @NotNull private Long categoryId;
}
