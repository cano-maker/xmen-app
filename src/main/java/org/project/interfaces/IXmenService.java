package org.project.interfaces;

import io.smallrye.mutiny.Uni;
import org.project.models.DNASequence;

public interface IXmenService {

    Uni<DNASequence> processADN(DNASequence DNASequence);
}
