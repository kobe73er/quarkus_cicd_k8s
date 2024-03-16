package org.interview.mapper;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.interview.dto.HttpErrorResponseDTO;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {

  @Override
  public Response toResponse(BadRequestException exception) {
    HttpErrorResponseDTO errorResponse = new HttpErrorResponseDTO("Invalid request");
    return Response.status(Response.Status.BAD_REQUEST)
        .entity(errorResponse)
        .type(MediaType.APPLICATION_JSON)
        .build();
  }
}
