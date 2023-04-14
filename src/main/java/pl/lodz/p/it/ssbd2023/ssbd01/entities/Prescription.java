package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.*;

import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(
        indexes = {
            @Index(name = "patient_data_index", columnList = "patient_data_id", unique = true)},
        uniqueConstraints = {
            @UniqueConstraint(columnNames = {"patient_data_id", "prescription_number"}),
})


public class Prescription extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "patient_data_id")
    private PatientData patientData;
    private String prescriptionNumber;

    @Builder
    public Prescription(String prescriptionNumber) {
        this.prescriptionNumber = prescriptionNumber;
    }

}
