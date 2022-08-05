package org.project;

import io.smallrye.mutiny.Uni;

import org.jboss.resteasy.reactive.RestResponse;
import org.jboss.resteasy.reactive.RestResponse.ResponseBuilder;
import org.project.interfaces.IXmenService;
import org.project.models.ADNSequence;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/mutant")
@ApplicationScoped
public class XmenResource {

    private final IXmenService xmenService;

    public XmenResource(IXmenService xmenService) {
        this.xmenService = xmenService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> mutant(ADNSequence sequence) {
        return xmenService.processADN(sequence)
                .onItem().transform(result -> generateOk(result))
                .onFailure().recoverWithItem(throwable -> generateError(throwable.getMessage()));
    }

    private static Response generateOk(String result)
    {
        return Response.status(Response.Status.OK)
                .entity(result)
                .build();
    }

    private static Response generateError(String message)
    {
        return Response.status(Response.Status.FORBIDDEN)
                .entity(message)
                .build();
    }


}