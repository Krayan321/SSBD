package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
public class Shipment extends AbstractEntity implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Setter
    private Date shipmentDate;

    @OneToMany
    @Getter
    @Setter
    private List<ShipmentMedication> shipmentMedications;
}
