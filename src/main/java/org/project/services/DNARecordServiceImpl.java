package org.project.services;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;
import org.project.entities.DNARecord;
import org.project.interfaces.IDNARecordService;
import org.project.models.DNAStats;
import org.project.respository.DNARecordRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class DNARecordServiceImpl implements IDNARecordService {

    private final DNARecordRepository dnaRepository;

    public DNARecordServiceImpl(DNARecordRepository dnaRepository) {
        this.dnaRepository = dnaRepository;
    }

    @Override
    @ReactiveTransactional
    public Uni<DNARecord> persistDNARecord(DNARecord dnRecord) {
        return dnaRepository.persist(dnRecord);
    }

    @Override
    public Uni<DNAStats> getStats() {
        return dnaRepository.listAll()
                .map(this::createDNAStats);
    }

    private DNAStats createDNAStats(List<DNARecord> dnaRecords)
    {
        DNAStats dnaStats = new DNAStats();
        dnaStats.setCountHumanDna(countHuman(dnaRecords));
        dnaStats.setCountMutantDna(countMutant(dnaRecords));
        dnaStats.setRatio(calculateRation(dnaStats.getCountMutantDna(), dnaStats.getCountHumanDna()));
        return dnaStats;

    }
    private Long countHuman(List<DNARecord> dnaRecords)
    {
        return dnaRecords.stream()
                .map(DNARecord::isMutant)
                .filter(Boolean.FALSE::equals)
                .collect(Collectors.counting());
    }

    private Long countMutant(List<DNARecord> dnaRecords)
    {
        return dnaRecords.stream()
                .map(DNARecord::isMutant)
                .filter(Boolean.TRUE::equals)
                .collect(Collectors.counting());
    }

    private Double calculateRation(Long countMutantDna, Long countHumanDna)
    {
        return Optional.empty()
                .map(o -> (double)countMutantDna/countHumanDna)
                .orElse(0.0);
    }



}
