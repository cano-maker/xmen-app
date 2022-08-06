package org.project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.project.exceptions.DNASequenceNotValid;
import org.project.models.ADNSequence;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class XmenServiceImplTest {

    private XmenServiceImpl underTest;


    @BeforeEach
    void setUp()
    {
        underTest = new XmenServiceImpl();
    }

    @Test
    void shouldFailWhenTheSizeLowThanFour()
    {
        ADNSequence model = getAdnSequenceLowThanFour();

        var result = underTest.processADN(model).await();

        DNASequenceNotValid exception = assertThrows(DNASequenceNotValid.class, result::indefinitely);

        assertEquals("Error, the size cant be lower than four", exception.getMessage());

    }

    @Test
    void shouldFailWhenTheSizeOfEachItemsIsNotTheSameThatTheSizeOfTheList()
    {
        ADNSequence model = getAdnSequenceUpperThanFour();

        var result = underTest.processADN(model).await();

        DNASequenceNotValid exception = assertThrows(DNASequenceNotValid.class, result::indefinitely);

        assertEquals("Error, the secuence is not valid", exception.getMessage());

    }

    private ADNSequence getAdnSequenceLowThanFour()
    {
       List<String> ADNSequence = List.of("ATGCGA","CAGTGC","TTATGT");
       return new ADNSequence(ADNSequence);
    }

    private ADNSequence getAdnSequenceUpperThanFour()
    {
        List<String> ADNSequence = List.of("ATGGA","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG");
        return new ADNSequence(ADNSequence);
    }

}