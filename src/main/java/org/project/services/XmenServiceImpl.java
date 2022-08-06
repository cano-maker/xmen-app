package org.project.services;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.project.enums.NumbersEnum;
import org.project.exceptions.DNASequenceNotValid;
import org.project.interfaces.IXmenService;
import org.project.models.ADNSequence;
import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;
import java.util.stream.Collectors;

@ApplicationScoped
public class XmenServiceImpl implements IXmenService {

    @Override
    public Uni<ADNSequence> processADN(ADNSequence adnSequence) {
        return validateSizeLowThanFour(adnSequence)
                .onItem().transform(aLong -> validateSequence(aLong, adnSequence))
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
                .allMatch(s -> s.length() == size);
    }
}
