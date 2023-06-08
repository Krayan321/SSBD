package pl.lodz.p.it.ssbd2023.ssbd01.dto.medication;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AbstractEntityDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.category.CategoryDTO;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MedicationDTO extends AbstractEntityDTO {

    @NotNull
    private String name;

    @NotNull
    private Integer stock;

    @NotNull
    private BigDecimal price;

    @NotNull
    private CategoryDTO categoryDTO;

    @Builder(builderMethodName = "medicationDTOBuilder")
    public MedicationDTO(Long id, Long version, String name, Integer stock, BigDecimal price, CategoryDTO categoryDTO) {
        super(id, version);
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.categoryDTO = categoryDTO;
    }
}
