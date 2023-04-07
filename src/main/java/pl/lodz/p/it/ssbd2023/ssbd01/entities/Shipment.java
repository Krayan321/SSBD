package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Shipment extends AbstractEntity implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(lombok.AccessLevel.NONE)
    private Long id;

    @NotNull
    @Basic(optional = false)
    private LocalDate shipmentDate;

    @OneToMany(mappedBy = "shipment", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "shipment_id")
    private List<ShipmentMedication> shipmentMedications;

}
