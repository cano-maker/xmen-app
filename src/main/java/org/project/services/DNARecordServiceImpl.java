package org.project.services;

import io.smallrye.mutiny.Uni;
import org.project.entities.DNARecord;
import org.project.interfaces.IDNARecordService;
import org.project.respository.DNARecordRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class DNARecordServiceImpl implements IDNARecordService {

    private final DNARecordRepository dnaRepository;

    public DNARecordServiceImpl(DNARecordRepository dnaRepository) {
        this.dnaRepository = dnaRepository;
    }

    @Override
    public Uni<DNARecord> persistDNARecord(DNARecord dnRecord) {
        return dnaRepository.persist(dnRecord);
    }
}
