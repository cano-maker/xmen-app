package org.project.services;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.project.entities.DNARecord;
import org.project.enums.NumbersEnum;
import org.project.exceptions.DNASequenceNotValid;
import org.project.interfaces.IValidationHorizontalSequence;
import org.project.interfaces.IXmenService;
import org.project.models.ADNSequence;
import org.project.respository.DNARecordRepository;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@ApplicationScoped
public class XmenServiceImpl implements IXmenService {

    private final IValidationHorizontalSequence validationHorizontalSequence;
    private final DNARecordRepository dnaRepository;


    public XmenServiceImpl(IValidationHorizontalSequence validationHorizontalSequence, DNARecordRepository dnaRepository) {
        this.validationHorizontalSequence = validationHorizontalSequence;
        this.dnaRepository = dnaRepository;
    }

    @Override
    public Uni<ADNSequence> processADN(ADNSequence adnSequence) {
        return validateSizeLowThanFour(adnSequence)
                .onItem().transform(aLong -> validateSequence(aLong, adnSequence))
                .onItem().transform(unused -> validateIfIsMutant(adnSequence))
                .onItem().transformToUni(this::persistResult)
                .onItem().transform(adnSequence1 -> validateResult(adnSequence1));
    }

    private ADNSequence validateResult(ADNSequence adnSequence)
    {
        return Optional.of(adnSequence.isMutant())
                .filter(Boolean.TRUE::equals)
                .map(unused -> adnSequence)
                .orElseThrow(() -> new DNASequenceNotValid("It's not mutant."));
    }

    @ReactiveTransactional
    public Uni<ADNSequence> persistResult(ADNSequence adnSequence)
    {
        return dnaRepository.persist(new DNARecord(adnSequence.isMutant()))
                .onItem().transform(unused -> adnSequence);
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
        return Optional.of(validationHorizontalSequence.processADN(adnSequence))
                .map(state -> setIsMutantAndKeepFlow(adnSequence, state))
                .orElse(adnSequence);
    }

    private ADNSequence setIsMutantAndKeepFlow(ADNSequence adnSequence, Boolean aBoolean) {
        adnSequence.setMutant(aBoolean);
        return adnSequence;
    }
}
