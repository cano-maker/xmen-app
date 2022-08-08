package org.project.services;


import org.project.enums.NumbersEnum;
import org.project.interfaces.IValidateObliqueSequence;
import org.project.interfaces.IValidationVerticalSequence;
import org.project.models.ADNSequence;
import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;


import static org.project.utils.ValidationUtils.*;

@ApplicationScoped
public class ValidationVerticalSequenceImpl implements IValidationVerticalSequence{

    private final IValidateObliqueSequence validateObliqueSequence;

    public ValidationVerticalSequenceImpl(IValidateObliqueSequence validateObliqueSequence) {
        this.validateObliqueSequence = validateObliqueSequence;
    }

    @Override
    public boolean processADN(ADNSequence adnSequence) {
        return Optional.of(validatePatternMutant(invertSequences(adnSequence)))
                .map(adnSequence::incrementCountMutantSequences)
                .filter(cant2 -> areIntegerUpperOrEqualsThan(cant2,NumbersEnum.MINIMUM_CANT_MUTANT.getValue()))
                .map(unused -> Boolean.TRUE )
                .orElseGet(() -> validateObliqueSequence.processADN(adnSequence));
    }
}
