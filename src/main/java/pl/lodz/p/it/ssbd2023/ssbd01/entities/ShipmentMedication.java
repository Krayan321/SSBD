package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "shipment_index", columnList = "shipment_id", unique = true),
        @Index(name = "medication_index", columnList = "medication_id", unique = true)
})
public class ShipmentMedication extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(lombok.AccessLevel.NONE)
    private Long id;

    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "shipment_id", updatable = false, nullable = false)
    private Shipment shipment;

    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "medication_id")
    private Medication medication;

    @Column(nullable = false)
    @NotNull
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;

}
