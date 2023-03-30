package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
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
