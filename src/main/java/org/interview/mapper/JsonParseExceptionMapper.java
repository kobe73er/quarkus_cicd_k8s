package org.interview.mapper;

import io.quarkus.logging.Log;
import io.vertx.core.json.JsonObject;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class JsonParseExceptionMapper implements ExceptionMapper<WebApplicationException> {

  @Override
  @Produces(MediaType.APPLICATION_JSON)
  public Response toResponse(WebApplicationException exception) {
    JsonObject json = new JsonObject();
    json.put("message", "Invalid JSON format");
    Log.error(exception.getMessage(), exception);

    return Response.status(Response.Status.BAD_REQUEST).entity(json.encode()).build();
  }
}
