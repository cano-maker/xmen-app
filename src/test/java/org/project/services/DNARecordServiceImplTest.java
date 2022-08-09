package org.project.services;

import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.project.entities.DNARecord;
import org.project.respository.DNARecordRepository;

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
}