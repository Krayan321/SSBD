package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "order_index", columnList = "order_id", unique = true),
        @Index(name = "prescription_index", columnList = "prescription_id", unique = true),
        @Index(name = "patient_data_index", columnList = "patient_data_id", unique = true),
        @Index(name = "chemist_data_index", columnList = "chemist_data_id", unique = true),
})
public class Order extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic(optional = false)
    @NotNull
    private Boolean inQueue;

    @Basic(optional = false)
    @NotNull
    private LocalDate orderDate;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "order_id")
    private List<OrderMedication> orderMedications;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "prescription_id")
    private List<Prescription> prescription;

    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "patient_data_id", referencedColumnName = "id")
    private PatientData patientData;

    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "chemist_data_id", referencedColumnName = "id")
    private ChemistData chemistData;

}
