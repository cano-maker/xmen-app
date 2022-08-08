//package org.project.services;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//import org.project.exceptions.DNASequenceNotValid;
//import org.project.models.ADNSequence;
//
//import java.util.List;
//import java.util.stream.Stream;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//
//class XmenServiceImplTest {
//
//    private XmenServiceImpl underTest;
//    private ValidationHorizontalSequenceImpl validationHorizontalSequence;
//
//
//    @BeforeEach
//    void setUp()
//    {
//        underTest = new XmenServiceImpl(validationHorizontalSequence);
//        //validationHorizontalSequence = mock(ValidationHorizontalSequence.class)
//    }
//
//    @Test
//    void shouldFailWhenTheSizeLowThanFour()
//    {
//        ADNSequence model = getAdnSequenceLowThanFour();
//
//        var result = underTest.processADN(model).await();
//
//        DNASequenceNotValid exception = assertThrows(DNASequenceNotValid.class, result::indefinitely);
//
//        assertEquals("Error, the size cant be lower than four", exception.getMessage());
//
//    }
//
//    @ParameterizedTest
//    @MethodSource("parameterFails")
//    void shouldFailWhenTheSizeOfEachItemsIsNotTheSameThatTheSizeOfTheList(List<String> sequence)
//    {
//        ADNSequence model = getAdnSequenceUpperThanFour(sequence);
//
//        var result = underTest.processADN(model).await();
//
//        DNASequenceNotValid exception = assertThrows(DNASequenceNotValid.class, result::indefinitely);
//
//        assertEquals("Error, the secuence is not valid", exception.getMessage());
//
//    }
//
//    @ParameterizedTest
//    @MethodSource("parametersWhenIsNotMutant")
//    void shouldFailWhenIsNotAMutant(List<String> sequence)
//    {
//        ADNSequence model = getAdnSequenceUpperThanFour(sequence);
//
//        var result = underTest.processADN(model).await();
//
//        DNASequenceNotValid exception = assertThrows(DNASequenceNotValid.class, result::indefinitely);
//
//        assertEquals("Is not a mutant!", exception.getMessage());
//
//    }
//
//    static Stream<Arguments> parametersWhenIsNotMutant(){
//        return Stream.of(
//                Arguments.arguments(List.of("AAAACG","CAGTGC","TTGTGT","AGAAGG","CTCCTA","TCACTG")),
//                Arguments.arguments(List.of("AAATCG","CAGTGC","TTATGT","AGATGG","CTCCTA","TCACTG"))
//        );
//    }
//
//    static Stream<Arguments> parameterFails(){
//        return Stream.of(
//                Arguments.arguments(List.of("ATGCG","CAG","TTATGT","AGAAGG","CCCC","TCACTG")),
//                Arguments.arguments(List.of("ATGCG","CAGTGKC","TTATGT","AGAAGG","CCCCTA","TCACTG")),
//                Arguments.arguments(List.of("ATGCG","CAGTGC","TTATGT","AGAAGG++","CCCCTA","TCACTG")),
//                Arguments.arguments(List.of("ATG454CG","CAGTGC","TTATGT","AGAAGG","CCCCTA","TCACTG")),
//                Arguments.arguments(List.of("ATGACGT","CAGTGCG","TTATGTG","AAG7GGA","CCCCTAG","TCACTGG","TCACTGG"))
//        );
//    }
//
//    private ADNSequence getAdnSequenceLowThanFour()
//    {
//       List<String> ADNSequence = List.of("ATGCGA","CAGTGC","TTATGT");
//       return new ADNSequence(ADNSequence);
//    }
//
//    private ADNSequence getAdnSequenceUpperThanFour(List<String> ADNSequence)
//    {
//        return new ADNSequence(ADNSequence);
//    }
//
//}