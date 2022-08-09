package org.project.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class DNARecord
{
    @Id
    @GeneratedValue
    private Long id;
    private boolean isMutant;

    public DNARecord(boolean isMutant) {
        this.isMutant = isMutant;
    }

    public DNARecord() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isMutant() {
        return isMutant;
    }

    public void setMutant(boolean mutant) {
        isMutant = mutant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DNARecord dnaRecord = (DNARecord) o;
        return isMutant == dnaRecord.isMutant && Objects.equals(id, dnaRecord.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isMutant);
    }
}
