package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@NamedQuery(name="shipment.findAll", query = "SELECT o FROM Shipment o")
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
