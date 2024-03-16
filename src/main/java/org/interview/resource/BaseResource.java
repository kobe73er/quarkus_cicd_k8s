package org.interview.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.interview.dto.BaseResponseDTO;
import org.interview.dto.HttpErrorResponseDTO;
import org.interview.dto.LookupResponseDTO;
import org.interview.service.EnvironmentService;

@Path("/")
public class BaseResource {

  @ConfigProperty(name = "application.version")
  String version;

  @Inject EnvironmentService environmentService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(
      description =
          "Provide the current date (UNIX epoch) and version. Additionally, a boolean property called Kubernetes should indicate if the application is running under Kubernetes. ")
  @APIResponse(
      responseCode = "200",
      description = "OK",
      content = @Content(schema = @Schema(implementation = LookupResponseDTO.class)))
  @APIResponse(
      responseCode = "400",
      description = "Bad Request",
      content = @Content(schema = @Schema(implementation = HttpErrorResponseDTO.class)))
  public BaseResponseDTO hello() {
    BaseResponseDTO response = new BaseResponseDTO();
    response.setVersion(version);
    response.setDate(System.currentTimeMillis() / 1000);
    response.setKubernetes(environmentService.isRunningInKubernetes());
    return response;
  }
}
