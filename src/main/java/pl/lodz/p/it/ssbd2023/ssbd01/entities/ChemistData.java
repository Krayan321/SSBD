package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@DiscriminatorValue("CHEMIST")
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
public class ChemistData extends AccessLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String licenseNumber;
}
