package pl.lodz.p.it.ssbd2023.ssbd01.dto.medication;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class MedicationDTO {

    @NotNull
    @Size(max = 50, min = 2)
    private String name;

    @NotNull
    private Integer stock;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Long categoryId;

    @Builder
    public MedicationDTO(String name, Integer stock, BigDecimal price, Long categoryId) {
        this.name = name;
        this.stock = stock;
        this.price = price;
        this.categoryId = categoryId;
    }
}
