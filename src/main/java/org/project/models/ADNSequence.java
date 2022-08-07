package org.project.models;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class ADNSequence
{
    private List<String> dna;

    @JsonIgnore
    private boolean isMutant;
    @JsonIgnore
    private int countMutantSequences;

    public ADNSequence() {}

    public ADNSequence(List<String> dna) {
        this.dna = dna;
    }

    public List<String> getDna() {
        return dna;
    }

    public void setDna(List<String> dna) {
        this.dna = dna;
    }

    public boolean isMutant() {
        return isMutant;
    }

    public void setMutant(boolean mutant) {
        isMutant = mutant;
    }

    public int getCountMutantSequences() {
        return countMutantSequences;
    }

    public int incrementCountMutantSequences(int cant)
    {
        return countMutantSequences=+cant;
    }

    public void setCountMutantSequences(int countMutantSequences) {
        this.countMutantSequences = countMutantSequences;
    }
}
