package org.interview.resource;

import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.interview.dto.ErrorMessageDTO;
import org.interview.dto.HttpErrorResponseDTO;
import org.interview.dto.LookupResponseDTO;
import org.interview.dto.ValidateIPRequestDTO;
import org.interview.dto.ValidateIPResponseDTO;
import org.interview.exception.DomainNotFoundException;
import org.interview.exception.InvalidDomainException;
import org.interview.service.DomainLookupService;
import org.interview.service.ToolsService;

@Path("/v1/tools")
@Tag(name = "Tools", description = "Tools API operations")
public class ToolsResource {

  @Inject ToolsService toolsService;

  @Inject DomainLookupService domainLookupService;

  @Path("/validate")
  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(
      summary = "Validate domain",
      description = "Validate if the input is an IPv4 address or not")
  @APIResponse(
      responseCode = "200",
      description = "OK",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = ValidateIPResponseDTO.class)))
  @APIResponse(
      responseCode = "400",
      description = "Bad Request",
      content =
          @Content(
              mediaType = MediaType.APPLICATION_JSON,
              schema = @Schema(implementation = HttpErrorResponseDTO.class)))
  public Response validateIP(@Valid ValidateIPRequestDTO request) {
    boolean isValid = toolsService.isValidIP(request.getIp());
    Log.info("IP " + request.getIp() + " is " + (isValid ? "valid" : isValid));
    return Response.ok(new ValidateIPResponseDTO(isValid)).build();
  }

  @GET
  @Path("/lookup")
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(
      summary = "Lookup domain",
      description = "Resolve ONLY the IPv4 addresses for the given domain")
  @APIResponses(
      value = {
        @APIResponse(
            responseCode = "200",
            description = "OK",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = LookupResponseDTO.class))),
        @APIResponse(
            responseCode = "400",
            description = "Bad Request",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessageDTO.class))),
        @APIResponse(
            responseCode = "404",
            description = "Not Found",
            content =
                @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = ErrorMessageDTO.class)))
      })
  public Response lookupDomain(
      @Parameter(description = "Domain name", required = true) @QueryParam("domain")
          String domain) {
    try {
      LookupResponseDTO response = domainLookupService.lookup(domain);
      return Response.ok(response).build();
    } catch (DomainNotFoundException e) {
      return Response.status(Response.Status.NOT_FOUND)
          .entity(new ErrorMessageDTO("Domain not found"))
          .build();
    } catch (InvalidDomainException e) {
      return Response.status(Response.Status.BAD_REQUEST)
          .entity(new ErrorMessageDTO("Invalid domain format"))
          .build();
    } catch (Exception e) {
      Log.error("Internal server error", e);
      return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
          .entity(new ErrorMessageDTO("Internal server error"))
          .build();
    }
  }
}
