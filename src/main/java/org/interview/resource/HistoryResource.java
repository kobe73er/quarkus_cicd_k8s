package org.interview.resource;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.interview.dto.DomainHistoryDTO;
import org.interview.dto.HttpErrorResponseDTO;
import org.interview.dto.LookupResponseDTO;
import org.interview.service.DomainLookupService;

@Path("/v1/history")
@Tag(name = "History")
public class HistoryResource {

  @Inject public DomainLookupService domainLookupService;

  @GET
  @Produces(MediaType.APPLICATION_JSON)
  @Operation(
      summary = "List queries",
      description =
          "Retrieve the latest 20 saved queries from the database and display them in order (the most recent first)")
  @APIResponse(
      responseCode = "200",
      description = "OK",
      content = @Content(schema = @Schema(implementation = LookupResponseDTO.class)))
  @APIResponse(
      responseCode = "400",
      description = "Bad Request",
      content = @Content(schema = @Schema(implementation = HttpErrorResponseDTO.class)))
  public Response getHistory() {
    List<DomainHistoryDTO> historyList = domainLookupService.getDomainHistory();
    return Response.ok(historyList).build();
  }
}
