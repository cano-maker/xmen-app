package org.project.resources;

import io.smallrye.mutiny.Uni;

import org.project.interfaces.IDNARecordService;
import org.project.interfaces.IXmenService;
import org.project.models.DNASequence;
import org.project.models.DNAStats;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("")
@ApplicationScoped
public class XmenResource {

    private final IXmenService xmenService;
    private final IDNARecordService dnaRecordRepository;

    public XmenResource(IXmenService xmenService, IDNARecordService dnaRecordRepository) {
        this.xmenService = xmenService;
        this.dnaRecordRepository = dnaRecordRepository;
    }

    @Path("/mutant")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> validateIfIsMutant(DNASequence sequence) {
        return xmenService.processADN(sequence)
                .onItem().transform(XmenResource::generateOk)
                .onFailure().recoverWithItem(throwable -> generateError(throwable.getMessage()));
    }

    @Path("/stats")
    @GET
    public Uni<Response> getStats()
    {
        return dnaRecordRepository.getStats()
                .map(XmenResource::generateOk)
                .onFailure().recoverWithItem(throwable -> generateError(throwable.getMessage()));
    }


    private static Response generateOk(DNAStats result)
    {
        return Response.status(Response.Status.OK)
                .entity(result)
                .build();
    }

    private static Response generateOk(DNASequence result)
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