package org.project.services;

import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.entities.DNARecord;
import org.project.models.DNAStats;
import org.project.respository.DNARecordRepository;
import org.project.utils.ValidationUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class DNARecordServiceImplTest {

    private DNARecordServiceImpl underTest;

    private DNARecordRepository dnaRepository;

    @BeforeEach
    void setUp() {

        dnaRepository = mock(DNARecordRepository.class);
        underTest = new DNARecordServiceImpl(dnaRepository);

    }

    @Test
    @DisplayName("Save dna record was succces")
    void shouldPersistSuccessfullyDnaRecord()
    {
        DNARecord dnaRecord = new DNARecord(Boolean.TRUE);

        when(dnaRepository.persist(dnaRecord)).thenReturn(Uni.createFrom().item(dnaRecord));

        var result = underTest.persistDNARecord(dnaRecord).await().indefinitely();

        assertEquals(dnaRecord, result);

        verify(dnaRepository, times(1)).persist(dnaRecord);
    }

    @Test
    @DisplayName("Return stats for dna records was success")
    void shouldReturnStatsSuccessfully()
    {
        DNAStats dnaStatsResult = new DNAStats(40L, 100L, 0.4);
        List<DNARecord> listDnaRecords = getDnaRecords();

        when(dnaRepository.listAll()).thenReturn(Uni.createFrom().item(listDnaRecords));

        var result = underTest.getStats().await().indefinitely();

        assertEquals(dnaStatsResult, result);

        verify(dnaRepository, times(1)).listAll();
    }

    private List<DNARecord> getDnaRecords()
    {
        List<DNARecord> listDnaRecordsHuman = getDnaRecordsHuman();
        List<DNARecord> listDnaRecordsMutant = getDnaRecordsMutant();

        return ValidationUtils.concatenate(listDnaRecordsMutant, listDnaRecordsHuman);

    }

    private List<DNARecord> getDnaRecordsMutant()
    {
        return IntStream.range(0, 40)
                .mapToObj(i -> new DNARecord(Boolean.TRUE))
                .collect(Collectors.toList());
    }

    private List<DNARecord> getDnaRecordsHuman()
    {
        return IntStream.range(0, 100)
                .mapToObj(i -> new DNARecord(Boolean.FALSE))
                .collect(Collectors.toList());
    }

}