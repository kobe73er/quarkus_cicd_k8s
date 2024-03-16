package org.interview.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.interview.dto.BaseResponseDTO;
import org.interview.service.EnvironmentService;

@Path("/")
public class BaseResource {

    @ConfigProperty(name = "application.version")
    String version;

    @Inject
    EnvironmentService environmentService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponseDTO hello() {
        BaseResponseDTO response = new BaseResponseDTO();
        response.setVersion(version);
        response.setDate(System.currentTimeMillis() / 1000);
        response.setKubernetes(environmentService.isRunningInKubernetes());
        return response;
    }

    @GET
    @Path("/new")
    @Produces(MediaType.APPLICATION_JSON)
    public String newapi(){
        return "new api";
    }

}
