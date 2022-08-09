package org.project.models;

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

}
