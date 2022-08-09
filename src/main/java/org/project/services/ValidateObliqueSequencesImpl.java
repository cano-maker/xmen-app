package org.project.services;

import org.project.enums.NumbersEnum;
import org.project.interfaces.IValidateObliqueSequence;
import org.project.models.DNASequence;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.project.utils.ValidationUtils.*;

@ApplicationScoped
public class ValidateObliqueSequencesImpl implements IValidateObliqueSequence
{
    @Override
    public boolean processADN(DNASequence dnaSequence) {

        return Optional.of(validatePatternMutant(getAllObliques(dnaSequence)))
                .map(dnaSequence::incrementCountMutantSequences)
                .filter(cant2 -> areIntegerUpperOrEqualsThan(cant2,NumbersEnum.MINIMUM_CANT_MUTANT.getValue()))
                .map(unused -> true)
                .orElse(false);
    }

    private List<String> getAllObliques(DNASequence dnaSequence) {
        var mainOblique = getObliquesInferiors(dnaSequence.getDna());
        var mainInvertOblique = getObliquesInferiorsInvert(dnaSequence.getDna(), dnaSequence.getDna().size() - 1);
        var obliquesInferiors = getListObliquesInferiors(dnaSequence.getDna());
        var obliquesInferiorsRevert = getListObliquesInferiorsInvert(dnaSequence.getDna());

        var verticalList = getVerticalList(dnaSequence);
        var obliquesSuperior = getListObliquesInferiors(verticalList);
        var verticalListInvert = getVerticalListInvert(dnaSequence);
        var obliquesSuperiorInvert = getListObliquesInferiors(verticalListInvert);

        obliquesInferiors.add(mainOblique);
        obliquesInferiorsRevert.add(mainInvertOblique);

        return concatenate(obliquesInferiors,obliquesInferiorsRevert,obliquesSuperior,obliquesSuperiorInvert);
    }

    private List<String> getVerticalList(DNASequence dnaSequence) {
        return IntStream.range(0, dnaSequence.getDna().size())
                .mapToObj(value -> getVerticalValues(dnaSequence, value))
                .collect(Collectors.toList());
    }

    private String getVerticalValues(DNASequence dnaSequence, int value) {
        return dnaSequence.getDna().stream()
                .map(s -> s.charAt(value))
                .map(Object::toString)
                .collect(Collectors.joining());
    }
    private List<String> getVerticalListInvert(DNASequence dnaSequence) {
        int start = 0;
        int end = dnaSequence.getDna().size();
        return IntStream.range(start,end)
                .map(i -> start + (end - 1 - i))
                .mapToObj(value -> getVerticalValues(dnaSequence, value))
                .collect(Collectors.toList());
    }

    private List<String> getListObliquesInferiorsInvert(List<String> adnSequence)
    {
        return IntStream.range(0, adnSequence.size() - NumbersEnum.MINIMUM_SIZE.getValue())
                .mapToObj(value -> getObliqueInferiorValuesInvert(adnSequence, ++value))
                .collect(Collectors.toList());
    }

    private String getObliqueInferiorValuesInvert(List<String> adnSequence, int value)
    {
        var sequences = adnSequence.subList(value, adnSequence.size());
        return getObliquesInferiorsInvert(sequences, adnSequence.size() - 1);
    }

    private List<String> getListObliquesInferiors(List<String> adnSequence) {
        return IntStream.range(0, adnSequence.size() - NumbersEnum.MINIMUM_SIZE.getValue())
                .mapToObj(value -> getObliqueInferiorValues(adnSequence, ++value))
                .collect(Collectors.toList());
    }

    private String getObliqueInferiorValues(List<String> adnSequence, int value)
    {
        var sequences = adnSequence.subList(value, adnSequence.size());
        return getObliquesInferiors(sequences);
    }

    private String getObliquesInferiorsInvert(List<String> sequences, int initValue) {

        AtomicInteger count = new AtomicInteger(initValue);
        return sequences.stream()
                .map(s -> s.charAt(count.getAndDecrement()))
                .map(Object::toString)
                .collect(Collectors.joining());
    }

    private String getObliquesInferiors(List<String> sequences) {
        AtomicInteger count = new AtomicInteger();
        return sequences.stream()
                .map(s -> s.charAt(count.getAndIncrement()))
                .map(Object::toString)
                .collect(Collectors.joining());
    }

}
