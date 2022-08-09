package org.project.services;

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
    public Uni<DNASequence> processADN(DNASequence dnaSequence) {
        return validateSizeLowThanFour(dnaSequence)
                .onItem().transform(aLong -> validateSequence(aLong, dnaSequence))
                .onItem().transform(unused -> validateIfIsMutant(dnaSequence))
                .onItem().transformToUni(this::persistResult)
                .onItem().transform(this::validateResult);
    }

    private DNASequence validateResult(DNASequence dnaSequence)
    {
        return Optional.of(dnaSequence.isMutant())
                .filter(Boolean.TRUE::equals)
                .map(unused -> dnaSequence)
                .orElseThrow(() -> new DNASequenceNotValid("It's not mutant."));
    }

    public Uni<DNASequence> persistResult(DNASequence dnaSequence)
    {
        return dnaRepository.persistDNARecord(new DNARecord(dnaSequence.isMutant()))
                .onItem().transform(unused -> dnaSequence);
    }

    private Uni<Long> validateSizeLowThanFour(DNASequence dnaSequence) {
        return Multi.createFrom().iterable(dnaSequence.getDna())
                .collect().with(Collectors.counting())
                .onItem().transform(this::validateSize);
    }

    private Long validateSize(Long size) {
        return Optional.ofNullable(size)
                .filter(aLong -> aLong >= NumbersEnum.MINIMUM_SIZE.getValue())
                .orElseThrow( () -> new DNASequenceNotValid("Error, the size cant be lower than four") );
    }

    private DNASequence validateSequence(Long size, DNASequence dnaSequence)
    {
        return Optional.of(isValidSequence(size, dnaSequence))
                .filter(Boolean.TRUE::equals)
                .map(unused -> dnaSequence)
                .orElseThrow(() -> new DNASequenceNotValid("Error, the secuence is not valid"));
    }

    private boolean isValidSequence(Long size, DNASequence dnaSequence) {
        return dnaSequence.getDna().stream()
                .map(String::toUpperCase)
                .allMatch(sequence -> ((sequence.length() == size) && validateCharacters(sequence)));
    }

    private boolean validateCharacters(String sequence) {
        Pattern pat = Pattern.compile("([ATGC])+");
        Matcher mat = pat.matcher(sequence);
        return mat.matches();
    }

    private DNASequence validateIfIsMutant(DNASequence dnaSequence)
    {
        return Optional.of(validationHorizontalSequence.processADN(dnaSequence))
                .map(state -> setIsMutantAndKeepFlow(dnaSequence, state))
                .orElse(dnaSequence);
    }

    private DNASequence setIsMutantAndKeepFlow(DNASequence dnaSequence, Boolean result) {
        dnaSequence.setMutant(result);
        return dnaSequence;
    }
}
