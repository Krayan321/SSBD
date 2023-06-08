package pl.lodz.p.it.ssbd2023.ssbd01.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.AbstractEntityDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.ChemistDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.PatientDataDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.prescrription.PrescriptionDTO;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.ChemistData;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.PatientData;
import pl.lodz.p.it.ssbd2023.ssbd01.entities.Prescription;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class OrderDTO extends AbstractEntityDTO {

    @NotNull
    private boolean inQueue;

    @NotNull
    private Date orderDate;

    @Builder
    public OrderDTO(
            Long id,
            Long version,
            boolean inQueue,
            Date orderDate
    ) {
        super(id, version);
        this.inQueue = inQueue;
        this.orderDate = orderDate;
    }
}
