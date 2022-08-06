package org.project.interfaces;

import io.smallrye.mutiny.Uni;
import org.project.models.ADNSequence;

public interface IXmenService {

    Uni<ADNSequence> processADN(ADNSequence adnSequence);
}
