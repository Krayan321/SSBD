package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@DiscriminatorValue("PATIENT")
@NoArgsConstructor
@ToString(callSuper = true)
public class PatientData extends AccessLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String pesel;

    private String firstName;

    private String lastName;

    private String phoneNumber;

    private String NIP;


}
