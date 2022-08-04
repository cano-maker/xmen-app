package org.project;

import io.smallrye.mutiny.Uni;
import org.project.models.ADNSequence;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/mutant")
@ApplicationScoped
public class XmenResource {

    @POST
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> mutant(ADNSequence sequence) {
        return Uni.createFrom().item(getMessage(sequence));
    }

    private static String getMessage(ADNSequence sequence) {
        return "The Sequence is: " + sequence.getDna().toString();
    }
}