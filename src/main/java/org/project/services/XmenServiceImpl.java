package org.project.services;

import io.smallrye.mutiny.Uni;
import org.project.interfaces.IXmenService;
import org.project.models.ADNSequence;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class XmenServiceImpl implements IXmenService {

    @Override
    public Uni<String> processADN(ADNSequence adnSequence) {
        return Uni.createFrom().item(adnSequence.getDna().toString());
    }
}
