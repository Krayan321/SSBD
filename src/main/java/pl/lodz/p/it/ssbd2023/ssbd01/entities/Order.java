package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@NamedQuery(name="order.findAll", query = "SELECT o FROM Order o")
public class Order extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Setter
    private Boolean inQueue;

    @Getter
    @Setter
    private LocalDateTime orderDate;

    @OneToMany
    @Getter
    @Setter
    private List<OrderMedication> orderMedications;

    @OneToMany
    @Getter
    @Setter
    private List<Prescription> prescription;

    @ManyToOne
    @Getter
    @Setter
    private PatientData patientData;

    @ManyToOne
    @Getter
    @Setter
    private ChemistData chemistData;

}
