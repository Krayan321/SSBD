package pl.lodz.p.it.ssbd2023.ssbd01.dto.category;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class CategoryDTO {

    @NotNull
    @Size(max = 50, min = 2)
    private String name;

    @NotNull
    private Boolean isOnPrescription;

}
