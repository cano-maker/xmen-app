package org.project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.project.exceptions.DNASizeLowThanFourException;
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

        DNASizeLowThanFourException exception = assertThrows(DNASizeLowThanFourException.class, result::indefinitely);

        assertEquals("Error, the size cant be lower than four", exception.getMessage());

    }

    private ADNSequence getAdnSequenceLowThanFour()
    {
       List<String> ADNSequence = List.of("ATGCGA","CAGTGC","TTATGT");
       return new ADNSequence(ADNSequence);
    }

}