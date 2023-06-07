package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
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
@Table(
        name = "patient_order",
        indexes = {
                @Index(name = "order_index", columnList = "id", unique = true),
                @Index(name = "prescription_index", columnList = "prescription_id", unique = true),
                @Index(name = "patient_data_index", columnList = "patient_data_id", unique = true),
                @Index(name = "chemist_data_index", columnList = "chemist_data_id", unique = true),
        })
@NamedQuery(
        name = "Order.findByPatientDataId",
        query =
                "SELECT o FROM Order o JOIN FETCH o.orderMedications WHERE o.patientData.id = :patientDataId")
public class Order extends AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(lombok.AccessLevel.NONE)
    private Long id;

    @Column(nullable = false, name = "in_queue")
    private Boolean inQueue;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, name = "order_date")
    private Date orderDate;

    @OneToMany(
            mappedBy = "order",
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.MERGE},
            orphanRemoval = true)
    private List<OrderMedication> orderMedications = new ArrayList<>();

    @OneToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "prescription_id", referencedColumnName = "id", updatable = false)
    private Prescription prescription;

    @ManyToOne(optional = false)
    @JoinColumn(
            name = "patient_data_id",
            referencedColumnName = "id",
            nullable = false,
            updatable = false)
    private PatientData patientData;

    @ManyToOne
    @JoinColumn(name = "chemist_data_id", referencedColumnName = "id", updatable = false)
    private ChemistData chemistData;
}
