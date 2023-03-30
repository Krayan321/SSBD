package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.io.Serializable;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@DiscriminatorValue("ADMIN")
@ToString(callSuper = true)
public class AdminData extends AccessLevel implements Serializable {

    private static final long serialVersionUID = 1L;

}
