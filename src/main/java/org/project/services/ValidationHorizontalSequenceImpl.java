package org.project.services;

import org.project.enums.NumbersEnum;
import org.project.interfaces.IValidationHorizontalSequence;
import org.project.interfaces.IValidationVerticalSequence;
import org.project.models.DNASequence;

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
    public boolean processADN(DNASequence DNASequence) {
        return Optional.of(validatePatternMutant(DNASequence.getDna()))
                .map(DNASequence::incrementCountMutantSequences)
                .filter(cant -> areIntegerUpperOrEqualsThan(cant,NumbersEnum.MINIMUM_CANT_MUTANT.getValue()))
                .map(unused -> true)
                .orElseGet(() -> validateVerticalSequences.processADN(DNASequence));
    }
}
