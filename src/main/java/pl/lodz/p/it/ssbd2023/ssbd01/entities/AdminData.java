package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serializable;

import jakarta.persistence.NamedQuery;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@NoArgsConstructor
@Table(name = "admin_data")
@DiscriminatorValue("ADMIN")
@ToString(callSuper = true)
@NamedQuery(name="adminData.findAll", query = "SELECT o FROM AdminData o")
public class AdminData extends AccessLevel implements Serializable {

    private static final long serialVersionUID = 1L;

    @Builder
    public AdminData(Long id) {
        super(id);
    }

}
