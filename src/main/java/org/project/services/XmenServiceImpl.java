package org.project.services;

import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.project.entities.DNARecord;
import org.project.enums.NumbersEnum;
import org.project.exceptions.DNASequenceNotValid;
import org.project.interfaces.IDNARecordService;
import org.project.interfaces.IValidationHorizontalSequence;
import org.project.interfaces.IXmenService;
import org.project.models.DNASequence;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@ApplicationScoped
public class XmenServiceImpl implements IXmenService {

    private final IValidationHorizontalSequence validationHorizontalSequence;
    private final IDNARecordService dnaRepository;


    public XmenServiceImpl(IValidationHorizontalSequence validationHorizontalSequence, IDNARecordService dnaRepository) {
        this.validationHorizontalSequence = validationHorizontalSequence;
        this.dnaRepository = dnaRepository;
    }

    @Override
    public Uni<DNASequence> processADN(DNASequence DNASequence) {
        return validateSizeLowThanFour(DNASequence)
                .onItem().transform(aLong -> validateSequence(aLong, DNASequence))
                .onItem().transform(unused -> validateIfIsMutant(DNASequence))
                .onItem().transformToUni(this::persistResult)
                .onItem().transform(adnSequence1 -> validateResult(adnSequence1));
    }

    private DNASequence validateResult(DNASequence DNASequence)
    {
        return Optional.of(DNASequence.isMutant())
                .filter(Boolean.TRUE::equals)
                .map(unused -> DNASequence)
                .orElseThrow(() -> new DNASequenceNotValid("It's not mutant."));
    }

    public Uni<DNASequence> persistResult(DNASequence DNASequence)
    {
        return dnaRepository.persistDNARecord(new DNARecord(DNASequence.isMutant()))
                .onItem().transform(unused -> DNASequence);
    }

    private Uni<Long> validateSizeLowThanFour(DNASequence DNASequence) {
        return Multi.createFrom().iterable(DNASequence.getDna())
                .collect().with(Collectors.counting())
                .onItem().transform(this::validateSize);
    }

    private Long validateSize(Long size) {
        return Optional.ofNullable(size)
                .filter(aLong -> aLong >= NumbersEnum.MINIMUM_SIZE.getValue())
                .orElseThrow( () -> new DNASequenceNotValid("Error, the size cant be lower than four") );
    }

    private DNASequence validateSequence(Long size, DNASequence DNASequence)
    {
        return Optional.of(isValidSequence(size, DNASequence))
                .filter(Boolean.TRUE::equals)
                .map(unused -> DNASequence)
                .orElseThrow(() -> new DNASequenceNotValid("Error, the secuence is not valid"));
    }

    private boolean isValidSequence(Long size, DNASequence DNASequence) {
        return DNASequence.getDna().stream()
                .map(String::toUpperCase)
                .allMatch(sequence -> ((sequence.length() == size) && validateCharacters(sequence)));
    }

    private boolean validateCharacters(String sequence) {
        Pattern pat = Pattern.compile("([ATGC])+");
        Matcher mat = pat.matcher(sequence);
        return mat.matches();
    }

    private DNASequence validateIfIsMutant(DNASequence DNASequence)
    {
        return Optional.of(validationHorizontalSequence.processADN(DNASequence))
                .map(state -> setIsMutantAndKeepFlow(DNASequence, state))
                .orElse(DNASequence);
    }

    private DNASequence setIsMutantAndKeepFlow(DNASequence DNASequence, Boolean aBoolean) {
        DNASequence.setMutant(aBoolean);
        return DNASequence;
    }
}
