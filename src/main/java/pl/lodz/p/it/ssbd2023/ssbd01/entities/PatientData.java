package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;

import jakarta.persistence.NamedQuery;
import lombok.*;
import pl.lodz.p.it.ssbd2023.ssbd01.dto.PatientDataDTO;

@Entity
@Getter
@Setter
@Table(name = "patient_data")
@DiscriminatorValue("PATIENT")
@NoArgsConstructor
@ToString(callSuper = true)
@NamedQuery(name="patientData.findAll", query = "SELECT o FROM PatientData o")
@AllArgsConstructor
public class PatientData extends AccessLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Builder
    public PatientData(Long id, String pesel, String firstName, String lastName, String phoneNumber, String NIP) {
        super(id);
        this.pesel = pesel;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.NIP = NIP;
    }

    @Column(nullable = false, unique = true)
    @NotNull
//    @Pattern(regexp = "^[0-9]{11}$", message = "Invalid PESEL")
    private String pesel;

    @Column(nullable = false, name = "first_name")
    @NotNull
    private String firstName;

    @Column(nullable = false, name = "last_name")
    @NotNull
    private String lastName;

    @Column(nullable = false, name = "phone_number")
    @NotNull
//    @Pattern(regexp = "^(\\+48)? ?[0-9]{3} ?[0-9]{3} ?[0-9]{3}$", message = "Invalid phone number")
    private String phoneNumber;

    @Column(nullable = false)
    @NotNull
//    @Pattern(regexp = "^[0-9]{3}-[0-9]{3}-[0-9]{2}-[0-9]{2}$", message = "Invalid NIP")
    private String NIP;
}
