package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
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
@Table(name = "shipment_medication",
        indexes = {
                @Index(name = "shipment_index", columnList = "shipment_id", unique = true),
                @Index(name = "medication_index", columnList = "medication_id", unique = true)
        })
public class ShipmentMedication extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(lombok.AccessLevel.NONE)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "shipment_id", updatable = false, nullable = false)
    private Shipment shipment;

    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "medication_id", updatable = false, nullable = false)
    private Medication medication;

    @Column(nullable = false)
    @NotNull
    @Min(value = 1, message = "Quantity must be greater than 0")
    private Integer quantity;

}
