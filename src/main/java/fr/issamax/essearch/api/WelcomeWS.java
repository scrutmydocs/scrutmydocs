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
		String helloMsg = "Welcome to the es-search API.<br/>Use <a href=\"scan/\">/scan</a> to scan dirs.";
		return Response.status(200).entity(helloMsg)
				.build();

	}

}