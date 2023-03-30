package pl.lodz.p.it.ssbd2023.ssbd01.entities;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Version;
import lombok.Getter;

@MappedSuperclass
public abstract class AbstractEntity {

    @Version
    @Getter
    private Long version;

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (this.getId() != null ? this.getId().hashCode() : 0);
        return hash;
    }

    public abstract Long getId();

    @Override
    public boolean equals(Object object) {
        if (object.getClass() != this.getClass()) {
            return false;
        }
        AbstractEntity other = (AbstractEntity) object;
        return (this.getId() != null || other.getId() == null) &&
                (this.getId() == null || this.getId().equals(other.getId()));
    }
}
