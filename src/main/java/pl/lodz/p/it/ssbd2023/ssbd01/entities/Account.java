package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@ToString
public class Account extends AbstractEntity implements Serializable {

    public static final long serialVersionUID = 1L;

    @OneToMany
    @Getter
    Set<AccessLevel> accessLevels = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Getter
    private String login;

    @Getter
    @Setter
    @ToString.Exclude
    private String password;

    @Getter
    @Setter
    private Boolean active;

    @Getter
    @Setter
    private Boolean registered;

    public Account(String login, String password) {
        this.login = login;
        this.password = password;
    }

}
