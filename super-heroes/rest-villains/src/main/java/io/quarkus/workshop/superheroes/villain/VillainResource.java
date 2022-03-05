package io.quarkus.workshop.superheroes.villain;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;

import org.jboss.logging.Logger;
import org.jboss.resteasy.reactive.RestPath;

import java.util.List;

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

    @GET
    @Path("/randow")
    public Response getRandomVillain(){
        Villain villain = service.findRandomVillain();
        logger.debugf("Found number of Villains"+villain);
        return Response.ok(villain).build();
    }

    @GET
    public Response getAllVillains(){
        List<Villain> villains = service.findAllVillains();
        logger.debugf("Total number of villains "+villains);
        return Response.ok(villains).build();
    }

    @GET
    @Path("/{id}")
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

    @POST
    public Response createVillain(@Valid Villain villain, @Context UriInfo uriInfo){
        villain = service.persistVillain(villain);
        UriBuilder builder = uriInfo.getAbsolutePathBuilder().path(Long.toString(villain.id));
        logger.debugf("Villain created with URI "+builder.build().toString());
        return Response.ok(villain).build();
    }

    @PUT
    public Response updateVillain(@Valid Villain villain) {
        villain = service.updateVilllain(villain);
        logger.debug("Villain updated with new valued " + villain);
        return Response.ok(villain).build();
    }

    @DELETE
    @Path("/{id}")
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
