package pl.lodz.p.it.ssbd2023.ssbd01.dto.order;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AbstractEntityDTO;
@Data
public class ChangeMedNumberDTO {

    @NotNull
    Long medicationId;

    @NotNull
    Integer quantity;

    @Builder
    ChangeMedNumberDTO(Long medicationId, Integer quantity) {
        this.medicationId = medicationId;
        this.quantity = quantity;
    }
}

