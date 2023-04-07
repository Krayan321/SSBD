package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@DiscriminatorValue("PATIENT")
@NoArgsConstructor
@ToString(callSuper = true)
public class PatientData extends AccessLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Basic(optional = false)
    @NotNull
    @Pattern(regexp = "^[0-9]{11}$", message = "Invalid PESEL")
    private String pesel;

    @Basic(optional = false)
    @NotNull
    private String firstName;

    @Basic(optional = false)
    @NotNull
    private String lastName;

    @Basic(optional = false)
    @NotNull
    @Pattern(regexp = "^(\\+48)? ?[0-9]{3} ?[0-9]{3} ?[0-9]{3}$", message = "Invalid phone number")
    private String phoneNumber;

    @Basic(optional = false)
    @NotNull
    @Pattern(regexp = "^[0-9]{3}-[0-9]{3}-[0-9]{2}-[0-9]{2}$", message = "Invalid NIP")
    private String NIP;


}
