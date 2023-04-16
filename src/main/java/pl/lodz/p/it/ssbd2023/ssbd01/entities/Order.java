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
@Getter
@Setter
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "order_index", columnList = "id", unique = true),
        @Index(name = "prescription_index", columnList = "prescription_id", unique = true),
        @Index(name = "patient_data_index", columnList = "patient_data_id", unique = true),
        @Index(name = "chemist_data_index", columnList = "chemist_data_id", unique = true),
})
public class Order extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private Boolean inQueue;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date orderDate;

    @OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.REFRESH, CascadeType.REMOVE}, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderMedication> orderMedications = new ArrayList<>();

    @OneToOne(optional = false, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "prescription_id", referencedColumnName = "id")
    private Prescription prescription;

    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "patient_data_id", referencedColumnName = "id", nullable = false, updatable = false)
    private PatientData patientData;

    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "chemist_data_id", referencedColumnName = "id")
    private ChemistData chemistData;

}
