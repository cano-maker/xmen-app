package org.project.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.project.models.ADNSequence;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ValidationHorizontalSequenceImplTest {

    private ValidationHorizontalSequenceImpl underTest;

    private ValidationVerticalSequenceImpl validationVerticalSequence;

    @BeforeEach
    void setUp() {
        validationVerticalSequence = mock(ValidationVerticalSequenceImpl.class);
        underTest = new ValidationHorizontalSequenceImpl(validationVerticalSequence);

    }

    @ParameterizedTest
    @MethodSource("parametersWhenNoFoundMutantSequences")
    void shouldCallVerticalValidationWhenNotFondMutantSequence(List<String> sequences)
    {
        ADNSequence model = getAdnSequenceUpperThanFour(sequences);

        when(validationVerticalSequence.processADN(model)).thenReturn(Boolean.FALSE);

        var result = underTest.processADN(model);

        assertFalse(result);

        verify(validationVerticalSequence, times(1)).processADN(model);
    }

    @ParameterizedTest
    @MethodSource("parametersWhenFoundMutantSequences")
    void shouldReturnBooleanWhenFondMutantSequence(List<String> sequences)
    {
        ADNSequence model = getAdnSequenceUpperThanFour(sequences);

        var result = underTest.processADN(model);

        assertTrue(result);

        verify(validationVerticalSequence, times(0)).processADN(model);
    }

    static Stream<Arguments> parametersWhenNoFoundMutantSequences(){
        return Stream.of(
                Arguments.arguments(List.of("AAAACG","CAGTGC","TTGTGT","AGAAGG","CTCCTA","TCACTG")),
                Arguments.arguments(List.of("AAATCG","CAGTGC","TTATGT","AGATGG","CTCCTA","TCACTG"))
        );
    }

    static Stream<Arguments> parametersWhenFoundMutantSequences(){
        return Stream.of(
                Arguments.arguments(List.of("AAAACG","CCCCGC","TTGTGT","AGAAGG","CTCCTA","TCACTG")),
                Arguments.arguments(List.of("AAAACG","CAGTGC","TTATGT","AGATGG","CCCCTA","TCACTG"))
        );
    }

    private ADNSequence getAdnSequenceUpperThanFour(List<String> ADNSequence)
    {
        return new ADNSequence(ADNSequence);
    }

}