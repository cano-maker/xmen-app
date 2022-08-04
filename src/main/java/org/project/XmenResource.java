package org.project;

import io.smallrye.mutiny.Uni;
import org.project.interfaces.IXmenService;
import org.project.models.ADNSequence;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/mutant")
@ApplicationScoped
public class XmenResource {

    private final IXmenService xmenService;

    public XmenResource(IXmenService xmenService) {
        this.xmenService = xmenService;
    }

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> mutant(ADNSequence sequence) {
        return xmenService.processADN(sequence);
    }

}