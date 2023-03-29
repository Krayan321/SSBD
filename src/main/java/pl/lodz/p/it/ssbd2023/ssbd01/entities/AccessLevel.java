package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.ManyToOne;
import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@ToString(callSuper = true)
@NoArgsConstructor
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "access_level")
public class AccessLevel extends AbstractEntity implements Serializable {

    public static final Long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    @Enumerated(EnumType.STRING)
    private Role role;

    @Getter
    @Setter
    private Boolean active = true;

    @ManyToOne
    private Account account;

}
