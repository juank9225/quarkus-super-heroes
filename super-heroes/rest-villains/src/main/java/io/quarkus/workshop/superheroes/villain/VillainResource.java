package io.quarkus.workshop.superheroes.villain;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestPath;

import java.net.URI;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("/api/villains")
public class VillainResource {

    @Inject
    Logger logger;
    @Inject
    VillainsService service;

    /*public VillainResource(Logger logger,VillainsService service){
        this.service = service;
        this.logger = logger;
    }*/

    @Operation(
	summary = "Returns a random villain")
    @GET
    @Path("/randow")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Villain.class, required = true)))
    public Response getRandomVillain(){
        Villain villain = service.findRandomVillain();
        logger.debugf("Found number of Villains"+villain);
        return Response.ok(villain).build();
    }

    @Operation(summary = "Returns all the villains from the database")
    @GET
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Villain.class, type = SchemaType.ARRAY)))
    @APIResponse(responseCode = "204", description = "No villains")
    public Response getAllVillains(){
        List<Villain> villains = service.findAllVillains();
        logger.debugf("Total number of villains "+villains);
        return Response.ok(villains).build();
    }

    @Operation(summary = "Returns a villain for a given identifier")
    @GET
    @Path("/{id}")
    @APIResponse(responseCode = "200", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Villain.class)))
    @APIResponse(responseCode = "204", description = "The villain is not found for a given identifier")
    public Response getVillain(@RestPath Long id){
        Villain villain = service.findVillainById(id);
        if (villain != null){
            logger.debugf("Found villain "+villain);
            return Response.ok(villain).build();
        }else {
            logger.debugf("No villains found with id"+id);
            return Response.noContent().build();
        }
    }
    @Operation(summary = "Creates a valid villain")
    @POST
    @APIResponse(responseCode = "201", description = "The URI of the created villain", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = URI.class)))
    public Response createVillain(@Valid Villain villain, @Context UriInfo uriInfo){
        villain = service.persistVillain(villain);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(villain.id));
        logger.debugf("Villain created with URI "+builder.build().toString());
        return Response.ok(villain).build();
    }

    @Operation(summary = "Updates an exiting  villain")
    @PUT
    @APIResponse(responseCode = "200", description = "The updated villain", content = @Content(mediaType = APPLICATION_JSON, schema = @Schema(implementation = Villain.class)))
    public Response updateVillain(@Valid Villain villain) {
        villain = service.updateVilllain(villain);
        logger.debug("Villain updated with new valued " + villain);
        return Response.ok(villain).build();
    }

    @Operation(summary = "Deletes an exiting villain")
    @DELETE
    @Path("/{id}")
    @APIResponse(responseCode = "204")
    public Response deleteVillain(@RestPath Long id) {
        service.deleteVillain(id);
        logger.debug("Villain deleted with " + id);
        return Response.noContent().build();//Respuesta sin un contexto
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello RESTEasy Reactive";
    }
}
