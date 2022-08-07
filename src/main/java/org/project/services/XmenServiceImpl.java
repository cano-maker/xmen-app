package org.project.services;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.project.enums.NumbersEnum;
import org.project.exceptions.DNASequenceNotValid;
import org.project.interfaces.IXmenService;
import org.project.models.ADNSequence;
import javax.enterprise.context.ApplicationScoped;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@ApplicationScoped
public class XmenServiceImpl implements IXmenService {

    @Override
    public Uni<ADNSequence> processADN(ADNSequence adnSequence) {
        return validateSizeLowThanFour(adnSequence)
                .onItem().transform(aLong -> validateSequence(aLong, adnSequence))
                .onItem().transform(unused -> validateIfIsMutant(adnSequence));
    }

    private Uni<Long> validateSizeLowThanFour(ADNSequence adnSequence) {
        return Multi.createFrom().iterable(adnSequence.getDna())
                .collect().with(Collectors.counting())
                .onItem().transform(this::validateSize);
    }

    private Long validateSize(Long size) {
        return Optional.ofNullable(size)
                .filter(aLong -> aLong >= NumbersEnum.MINIMUM_SIZE.getValue())
                .orElseThrow( () -> new DNASequenceNotValid("Error, the size cant be lower than four") );
    }

    private ADNSequence validateSequence(Long size, ADNSequence adnSequence)
    {
        return Optional.of(isValidSequence(size, adnSequence))
                .filter(Boolean.TRUE::equals)
                .map(unused -> adnSequence)
                .orElseThrow(() -> new DNASequenceNotValid("Error, the secuence is not valid"));
    }

    private boolean isValidSequence(Long size, ADNSequence adnSequence) {
        return adnSequence.getDna().stream()
                .map(String::toUpperCase)
                .allMatch(sequence -> ((sequence.length() == size) && validateCharacters(sequence)));
    }

    private boolean validateCharacters(String sequence) {
        Pattern pat = Pattern.compile("([ATGC])+");
        Matcher mat = pat.matcher(sequence);
        return mat.matches();
    }

    private ADNSequence validateIfIsMutant(ADNSequence adnSequence)
    {
        return Optional.of(validateHorizontalSequences(adnSequence))
                .filter(Boolean.TRUE::equals)
                .map(state -> getaVoid(adnSequence, state))
                .orElseThrow(() -> new DNASequenceNotValid("Is not a mutant!"));
    }

    private ADNSequence getaVoid(ADNSequence adnSequence, Boolean aBoolean) {
        adnSequence.setMutant(aBoolean);
        return adnSequence;
    }

    private boolean validateHorizontalSequences(ADNSequence adnSequence)
    {
        return Optional.of(validatePatternMutant(adnSequence.getDna()))
                .map(Long::intValue)
                .map(adnSequence::incrementCountMutantSequences)
                .filter(cant -> cant >= NumbersEnum.MINIMUM_CANT_MUTANT.getValue())
                .map(unused -> Boolean.TRUE)
                .orElseGet(() -> validateVerticalSequences(adnSequence));
    }

    private Long validatePatternMutant(List<String> adnSequence) {
        return adnSequence.stream()
                .filter(this::validateSequenceMutant)
                .collect(Collectors.counting());
    }

    private boolean validateSequenceMutant(String sequence)
    {
        Pattern pat = Pattern.compile("(.)\\1{3}");
        Matcher mat = pat.matcher(sequence);
        return mat.find();
    }

    private boolean validateVerticalSequences(ADNSequence adnSequence)
    {
        return Optional.of(validatePatternMutant(getVerticalList(adnSequence)))
                .map(Long::intValue)
                .map(adnSequence::incrementCountMutantSequences)
                .filter(cant2 -> cant2 >= NumbersEnum.MINIMUM_CANT_MUTANT.getValue())
                .map(unused -> Boolean.TRUE )
                .orElseGet(() -> validateObliqueUpSequences(adnSequence));
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

    private boolean validateObliqueUpSequences(ADNSequence adnSequence) {

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

        obliquesInferiors.addAll(obliquesSuperior);
        obliquesInferiors.addAll(obliquesInferiorsRevert);
        obliquesInferiors.addAll(obliquesSuperiorInvert);

        return Optional.of(validatePatternMutant(obliquesInferiors))
                .map(Long::intValue)
                .map(adnSequence::incrementCountMutantSequences)
                .filter(cant2 -> cant2 >= NumbersEnum.MINIMUM_CANT_MUTANT.getValue())
                .map(unused -> Boolean.TRUE )
                .orElse(Boolean.FALSE);
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
