package fr.issamax.essearch.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Component;

@Component
@Path("/")
public class WelcomeWS {

	@GET
	public Response welcome() {

		return Response.status(200).entity("Welcome to the es-search API")
				.build();

	}

}