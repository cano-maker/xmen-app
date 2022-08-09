package org.project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.project.models.DNASequence;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidationVerticalSequenceImplTest {

    private ValidationVerticalSequenceImpl underTest;

    private ValidateObliqueSequencesImpl validateObliqueSequences;

    @BeforeEach
    void setUp() {
        validateObliqueSequences = mock(ValidateObliqueSequencesImpl.class);
        underTest = new ValidationVerticalSequenceImpl(validateObliqueSequences);

    }
    @ParameterizedTest
    @MethodSource("parametersWhenNoFoundMutantSequences")
    void shouldCallVerticalValidationWhenNotFondMutantSequence(List<String> sequences)
    {
        DNASequence model = getAdnSequenceUpperThanFour(sequences);

        when(validateObliqueSequences.processADN(model)).thenReturn(Boolean.FALSE);

        var result = underTest.processADN(model);

        assertFalse(result);

        verify(validateObliqueSequences, times(1)).processADN(model);
    }

    @ParameterizedTest
    @MethodSource("parametersWhenFoundMutantSequences")
    void shouldReturnBooleanWhenFondMutantSequence(List<String> sequences)
    {
        DNASequence model = getAdnSequenceUpperThanFour(sequences);

        var result = underTest.processADN(model);

        assertTrue(result);

        verify(validateObliqueSequences, times(0)).processADN(model);
    }

    static Stream<Arguments> parametersWhenNoFoundMutantSequences(){
        return Stream.of(
                Arguments.arguments(List.of("AAAACG","CAGTGC","TTGTGT","AGAAGG","CTCCTA","TCACTG")),
                Arguments.arguments(List.of("AAATCG","CAGTGC","TTATGT","AGATGG","CTCCTA","TCACTG"))
        );
    }

    static Stream<Arguments> parametersWhenFoundMutantSequences(){
        return Stream.of(
                Arguments.arguments(List.of("AAADCG","ACCCCC","ATGTCT","AGAACG","CTCCCA","TCACTG")),
                Arguments.arguments(List.of("ADAACG","CDGTGG","TDATGG","ADATGG","CDCCTA","TCACTG"))
        );
    }

    private DNASequence getAdnSequenceUpperThanFour(List<String> ADNSequence)
    {
        return new DNASequence(ADNSequence);
    }

}