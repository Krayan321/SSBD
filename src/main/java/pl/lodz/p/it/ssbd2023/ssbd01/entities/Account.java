package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@ToString
@Getter
@Setter
public class Account extends AbstractEntity implements Serializable {

    public static final long serialVersionUID = 1L;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, mappedBy = "account")
    Set<AccessLevel> accessLevels = new HashSet<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(lombok.AccessLevel.NONE)
    private Long id;

    private String login;

    @ToString.Exclude
    private String password;

    private Boolean active;

    private Boolean registered;

    @Builder
    public Account(String login, String password) {
        this.login = login;
        this.password = password;
    }

}
