package org.project.services;

import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.project.entities.DNARecord;
import org.project.exceptions.DNASequenceNotValid;
import org.project.interfaces.IDNARecordService;
import org.project.models.ADNSequence;
import org.project.respository.DNARecordRepository;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;




class XmenServiceImplTest {

    private XmenServiceImpl underTest;
    private ValidationHorizontalSequenceImpl validationHorizontalSequence;
    private DNARecordServiceImpl dnaRecordRepository;


    @BeforeEach
    void setUp()
    {
        validationHorizontalSequence = mock(ValidationHorizontalSequenceImpl.class);
        dnaRecordRepository = mock(DNARecordServiceImpl.class);
        underTest = new XmenServiceImpl(validationHorizontalSequence, dnaRecordRepository);
    }

    @Test
    void shouldFailWhenTheSizeLowThanFour()
    {
        ADNSequence model = getAdnSequenceLowThanFour();
        var result = underTest.processADN(model).await();
        DNASequenceNotValid exception = assertThrows(DNASequenceNotValid.class, result::indefinitely);
        assertEquals("Error, the size cant be lower than four", exception.getMessage());

    }

    @ParameterizedTest
    @MethodSource("parameterFails")
    void shouldFailWhenTheSizeOfEachItemsIsNotTheSameThatTheSizeOfTheList(List<String> sequence)
    {
        ADNSequence model = getAdnSequenceUpperThanFour(sequence);
        var result = underTest.processADN(model).await();
        DNASequenceNotValid exception = assertThrows(DNASequenceNotValid.class, result::indefinitely);
        assertEquals("Error, the secuence is not valid", exception.getMessage());

    }

    @Test
    void shouldFailWhenIsNotAMutant()
    {
        DNARecord entity = new DNARecord(Boolean.FALSE);
        ADNSequence model = getAdnSequenceUpperThanFour();
        when(validationHorizontalSequence.processADN(model)).thenReturn(Boolean.FALSE);
        when(dnaRecordRepository.persistDNARecord(entity)).thenReturn(Uni.createFrom().item(entity));

        var result = underTest.processADN(model).await();
        DNASequenceNotValid exception = assertThrows(DNASequenceNotValid.class, result::indefinitely);

        assertEquals("It's not mutant.", exception.getMessage());
        verify(validationHorizontalSequence, times(1)).processADN(model);
        verify(dnaRecordRepository, times(1)).persistDNARecord(entity);

    }

    @Test
    void shouldSuccessWhenIsAMutant()
    {
        DNARecord entity = new DNARecord(Boolean.TRUE);
        ADNSequence model = getAdnSequenceUpperThanFour();
        model.setMutant(Boolean.TRUE);

        when(validationHorizontalSequence.processADN(model)).thenReturn(Boolean.TRUE);
        when(dnaRecordRepository.persistDNARecord(entity)).thenReturn(Uni.createFrom().item(entity));

        var result = underTest.processADN(model).await().indefinitely();

        assertEquals(model.isMutant(), result.isMutant());
        verify(validationHorizontalSequence, times(1)).processADN(model);
        verify(dnaRecordRepository, times(1)).persistDNARecord(entity);

    }
    static Stream<Arguments> parameterFails(){
        return Stream.of(
                Arguments.arguments(List.of("ATGCG","CAG","TTATGT","AGAAGG","CCCC","TCACTG")),
                Arguments.arguments(List.of("ATGCG","CAGTGKC","TTATGT","AGAAGG","CCCCTA","TCACTG")),
                Arguments.arguments(List.of("ATGCG","CAGTGC","TTATGT","AGAAGG++","CCCCTA","TCACTG")),
                Arguments.arguments(List.of("ATG454CG","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG")),
                Arguments.arguments(List.of("ATGACGT","CAGTGCG","TTATGTG","AAG7GGA","CCCCTAG","TCACTGG","TCACTGG")),
                Arguments.arguments(List.of("AAADCG","ACCCCC","ATGTCT","AGAACG","CTCCCA","TCACTG"))
        );
    }

    private ADNSequence getAdnSequenceLowThanFour()
    {
       List<String> ADNSequence = List.of("ATGCGA","CAGTGC","TTATGT");
       return new ADNSequence(ADNSequence);
    }

    private ADNSequence getAdnSequenceUpperThanFour()
    {
        List<String> ADNSequence = List.of("AAAACG","CAGTGC","TTGTGT","AGAAGG","CTCCTA","TCACTG");
        return new ADNSequence(ADNSequence);
    }

    private ADNSequence getAdnSequenceUpperThanFour(List<String> ADNSequence)
    {
        return new ADNSequence(ADNSequence);
    }

}