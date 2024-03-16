package org.interview.mapper;

import io.quarkus.logging.Log;
import io.vertx.core.json.JsonObject;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

  @Override
  @Produces(MediaType.APPLICATION_JSON)
  public Response toResponse(Exception exception) {
    JsonObject json = new JsonObject();
    json.put("message", "An unexpected error occurred. Please try again later.");
    Log.error(exception.getMessage(), exception);

    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(json.encode()).build();
  }
}
