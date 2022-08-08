package org.project.services;

import org.project.enums.NumbersEnum;
import org.project.interfaces.IValidationHorizontalSequence;
import org.project.interfaces.IValidationVerticalSequence;
import org.project.models.ADNSequence;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

import static org.project.utils.ValidationUtils.*;

@ApplicationScoped
public class ValidationHorizontalSequenceImpl implements IValidationHorizontalSequence
{

    private final IValidationVerticalSequence validateVerticalSequences;

    public ValidationHorizontalSequenceImpl(IValidationVerticalSequence validateVerticalSequences) {
        this.validateVerticalSequences = validateVerticalSequences;
    }

    @Override
    public boolean processADN(ADNSequence adnSequence) {
        return Optional.of(validatePatternMutant(adnSequence.getDna()))
                .map(adnSequence::incrementCountMutantSequences)
                .filter(cant -> areIntegerUpperOrEqualsThan(cant,NumbersEnum.MINIMUM_CANT_MUTANT.getValue()))
                .map(unused -> Boolean.TRUE)
                .orElseGet(() -> validateVerticalSequences.processADN(adnSequence));
    }
}
