package org.project.models;

public class DNAStats
{
    private Long countMutantDna;
    private Long countHumanDna;
    private Integer ratio;

    public DNAStats(Long countMutantDna, Long countHumanDna, Integer ratio) {
        this.countMutantDna = countMutantDna;
        this.countHumanDna = countHumanDna;
        this.ratio = ratio;
    }

    public DNAStats() {
    }

}
