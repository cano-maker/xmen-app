package org.project.models;

import java.util.Objects;

public class DNAStats
{
    private Long countMutantDna;
    private Long countHumanDna;
    private Double ratio;

    public DNAStats(Long countMutantDna, Long countHumanDna, Double ratio) {
        this.countMutantDna = countMutantDna;
        this.countHumanDna = countHumanDna;
        this.ratio = ratio;
    }

    public DNAStats() {
    }

    public Long getCountMutantDna() {
        return countMutantDna;
    }

    public Long getCountHumanDna() {
        return countHumanDna;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setCountMutantDna(Long countMutantDna) {
        this.countMutantDna = countMutantDna;
    }

    public void setCountHumanDna(Long countHumanDna) {
        this.countHumanDna = countHumanDna;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DNAStats dnaStats = (DNAStats) o;
        return Objects.equals(countMutantDna, dnaStats.countMutantDna) && Objects.equals(countHumanDna, dnaStats.countHumanDna) && Objects.equals(ratio, dnaStats.ratio);
    }

    @Override
    public int hashCode() {
        return Objects.hash(countMutantDna, countHumanDna, ratio);
    }
}
