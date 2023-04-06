package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.io.Serializable;

import jakarta.persistence.NamedQuery;
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
@NamedQuery(name="chemistData.findAll", query = "SELECT o FROM ChemistData o")
public class ChemistData extends AccessLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    private String licenseNumber;
}
