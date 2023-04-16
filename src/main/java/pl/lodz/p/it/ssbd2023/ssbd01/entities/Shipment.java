package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(indexes = {
        @Index(name = "shipment_index", columnList = "id", unique = true),
})
@NamedQuery(name="shipment.findAll", query = "SELECT o FROM Shipment o")
public class Shipment extends AbstractEntity implements Serializable {

    public static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(lombok.AccessLevel.NONE)
    private Long id;

    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date shipmentDate;

    @OneToMany(mappedBy = "shipment", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, orphanRemoval = true)
    @JoinColumn(name = "shipment_id")
    private List<ShipmentMedication> shipmentMedications = new ArrayList<>();

}
