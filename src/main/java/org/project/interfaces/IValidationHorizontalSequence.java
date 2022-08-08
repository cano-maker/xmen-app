package org.project.interfaces;

import io.smallrye.mutiny.Uni;
import org.project.models.ADNSequence;

public interface IValidationHorizontalSequence {

    boolean processADN(ADNSequence adnSequence);
}
