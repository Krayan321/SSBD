package pl.lodz.p.it.ssbd2023.ssbd01.dto.medication;

import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Category;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EditMedicationDTO {

  @NotNull private Long version;

  @NotNull private Integer stock;

  @NotNull private String name;

  @NotNull private BigDecimal price;

  @NotNull private Category category;
}
