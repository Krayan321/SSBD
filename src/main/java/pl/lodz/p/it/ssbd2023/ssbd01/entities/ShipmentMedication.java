package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class ShipmentMedication extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @ManyToOne
    @Getter
    @Setter
    private Medication medication;

    @Getter
    @Setter
    private Integer quantity;

}
