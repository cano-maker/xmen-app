package org.project.services;

import org.project.enums.NumbersEnum;
import org.project.interfaces.IValidateObliqueSequence;
import org.project.models.ADNSequence;

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
    public boolean processADN(ADNSequence adnSequence) {

        return Optional.of(validatePatternMutant(getAllObliques(adnSequence)))
                .map(adnSequence::incrementCountMutantSequences)
                .filter(cant2 -> areIntegerUpperOrEqualsThan(cant2,NumbersEnum.MINIMUM_CANT_MUTANT.getValue()))
                .map(unused -> Boolean.TRUE )
                .orElse(Boolean.FALSE);
    }

    private List<String> getAllObliques(ADNSequence adnSequence) {
        var mainOblique = getObliquesInferiors(adnSequence.getDna());
        var mainInvertOblique = getObliquesInferiorsInvert(adnSequence.getDna(), adnSequence.getDna().size() - 1);
        var obliquesInferiors = getListObliquesInferiors(adnSequence.getDna());
        var obliquesInferiorsRevert = getListObliquesInferiorsInvert(adnSequence.getDna());

        var verticalList = getVerticalList(adnSequence);
        var obliquesSuperior = getListObliquesInferiors(verticalList);
        var verticalListInvert = getVerticalListInvert(adnSequence);
        var obliquesSuperiorInvert = getListObliquesInferiors(verticalListInvert);

        obliquesInferiors.add(mainOblique);
        obliquesInferiorsRevert.add(mainInvertOblique);

        return concatenate(obliquesInferiors,obliquesInferiorsRevert,obliquesSuperior,obliquesSuperiorInvert);
    }

    private List<String> getVerticalList(ADNSequence adnSequence) {
        return IntStream.range(0, adnSequence.getDna().size())
                .mapToObj(value -> getVerticalValues(adnSequence, value))
                .collect(Collectors.toList());
    }

    private String getVerticalValues(ADNSequence adnSequence, int value) {
        return adnSequence.getDna().stream()
                .map(s -> s.charAt(value))
                .map(Object::toString)
                .collect(Collectors.joining());
    }
    private List<String> getVerticalListInvert(ADNSequence adnSequence) {
        int start = 0;
        int end = adnSequence.getDna().size();
        return IntStream.range(start,end)
                .map(i -> start + (end - 1 - i))
                .mapToObj(value -> getVerticalValues(adnSequence, value))
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
