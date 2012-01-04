package fr.issamax.essearch.api;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

public class APINotFoundException extends WebApplicationException {
	private static final long serialVersionUID = 1L;

	/**
      * Create a HTTP 404 (Not Found) exception.
     */
     public APINotFoundException() {
         super(Response.status(Status.NOT_FOUND).build());
     }

     /**
      * Create a HTTP 404 (Not Found) exception.
      * @param message the String that is the entity of the 404 response.
      */
     public APINotFoundException(String message) {
         super(Response.status(Status.NOT_FOUND).entity(message).type("text/plain").build());
     }

}