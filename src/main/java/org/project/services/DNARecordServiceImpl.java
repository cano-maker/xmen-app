package org.project.services;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Uni;
import org.project.entities.DNARecord;
import org.project.interfaces.IDNARecordService;
import org.project.models.DNAStats;
import org.project.respository.DNARecordRepository;

import javax.enterprise.context.ApplicationScoped;

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
        return null;
    }
}
