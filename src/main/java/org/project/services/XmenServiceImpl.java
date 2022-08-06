package org.project.services;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.project.exceptions.DNASizeLowThanFourException;
import org.project.interfaces.IXmenService;
import org.project.models.ADNSequence;

import javax.enterprise.context.ApplicationScoped;
import java.util.stream.Collectors;

@ApplicationScoped
public class XmenServiceImpl implements IXmenService {

    @Override
    public Uni<ADNSequence> processADN(ADNSequence adnSequence) {
        return validateSizeLowThanFour(adnSequence)
                .onItem().transform(unused -> adnSequence);
    }

    private Uni<Long> validateSizeLowThanFour(ADNSequence adnSequence)
    {
        return Multi.createFrom().iterable(adnSequence.getDna())
                .collect().with(Collectors.counting())
                .onItem().transform(this::validateSize);

    }

    private Long validateSize(Long size)
    {
        return size < 4 ? throwSizeException() : size;
    }

    private int throwSizeException()
    {
        throw new DNASizeLowThanFourException("Error, the size cant be lower than four");
    }
}
