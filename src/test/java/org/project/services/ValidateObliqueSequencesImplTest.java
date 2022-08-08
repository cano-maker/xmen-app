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

class ValidateObliqueSequencesImplTest {

    private ValidateObliqueSequencesImpl underTest;

    @BeforeEach
    void setUp() {

        underTest = new ValidateObliqueSequencesImpl();
    }

    @ParameterizedTest
    @MethodSource("parametersWhenNoFoundMutantSequences")
    void shouldCallVerticalValidationWhenNotFondMutantSequence(List<String> sequences)
    {
        ADNSequence model = getAdnSequenceUpperThanFour(sequences);

        var result = underTest.processADN(model);

        assertFalse(result);

    }

    @ParameterizedTest
    @MethodSource("parametersWhenFoundMutantSequences")
    void shouldReturnBooleanWhenFondMutantSequence(List<String> sequences)
    {
        ADNSequence model = getAdnSequenceUpperThanFour(sequences);

        var result = underTest.processADN(model);

        assertTrue(result);

    }

    static Stream<Arguments> parametersWhenNoFoundMutantSequences(){
        return Stream.of(
                Arguments.arguments(List.of("AAAACG","CAGTGC","TTGTGT","AGAAGG","CTCCTA","TCACTG")),
                Arguments.arguments(List.of("AAATCG","CAGTGC","TTATGT","AGATGG","CTCCTA","TCACTG"))
        );
    }

    static Stream<Arguments> parametersWhenFoundMutantSequences(){
        return Stream.of(
                Arguments.arguments(List.of("AAADCG", "ACCCGC", "ATGGCT", "AGGCCG", "CGCTCA", "TCACTG")),
                Arguments.arguments(List.of("ADAACG", "CADTGG", "TDADGG", "ADAADG", "CDCCTA", "TCACTG"))
        );
    }

    private ADNSequence getAdnSequenceUpperThanFour(List<String> ADNSequence)
    {
        return new ADNSequence(ADNSequence);
    }

}