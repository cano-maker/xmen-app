package org.project.models;


import java.util.List;

public class ADNSequence
{
    private List<String> dna;

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
}
