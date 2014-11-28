package de.hsrm.swt02.restserver.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;


@Path("command/user")
public class UserCommandResource {

    @POST @Path("login")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes("application/x-www-form-urlencoded")
    public Response login (MultivaluedMap<String, String> formParams) {
        // TODO use logger
        String username = formParams.get("username").get(0);
        String password = formParams.get("password").get(0);
        System.out.println("LOGIN -> " + username + " : " + password);
        //TODO: check Login in logic
        return Response.ok().build();
    }
    
}
