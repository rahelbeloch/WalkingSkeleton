package de.hsrm.swt02.restserver.resource;

import java.io.IOException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.constructionfactory.ConstructionFactory;
import de.hsrm.swt02.messaging.ServerPublisher;
import de.hsrm.swt02.model.Workflow;

@Path("resource")
public class WorkflowResource {

    public static final Logic logic = ConstructionFactory.getLogic();
    public static final ServerPublisher publisher = ConstructionFactory.getPublisher();
    
	/**
	 * 
	 * @param workflowid
	 * @return the requested workflow
	 */	@GET @Path("workflow/{workflowid}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getWorkflow (@PathParam("workflowid") int workflowid) {
		System.out.println("GET -> " + workflowid);
		ObjectMapper mapper = new ObjectMapper();
		Workflow workflow = logic.getWorkflow(workflowid);
		String workflowAsString;
		try {
			workflowAsString = mapper.writeValueAsString(workflow);
		} catch (JsonProcessingException e) {
			return Response.serverError().build();
		}
		return Response.ok(workflowAsString).build();
	}
	
	/**
	 * 
	 * @param workflowid
	 * @return the requested workflow
	 */
	@GET @Path("workflows/{username}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getAllWorkflows (@PathParam("username") String username) {
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("GETALL -> " + username);
		List<Workflow>wflowList = logic.getWorkflowsByUser(logic.getUser(username));
		String wListString;
		try {
			wListString = mapper.writeValueAsString(wflowList);
		} catch (JsonProcessingException e) {
			return Response.serverError().entity("Jackson parsing-error").build();
		}
		System.out.println(wListString);
		return Response.ok(wListString).build();
	}
	
	/**
	 * 
	 * receives a workflow and stores it into the database
	 * 
	 * @param receivedWorkflow
	 * @return 	"true" if everything was successful OR
	 * 			"jackson exception" if serialization crashed
	 */
	@POST @Path("workflow")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes("application/x-www-form-urlencoded")
	public Response saveWorkflow (MultivaluedMap<String, String> formParams) {
		ObjectMapper mapper = new ObjectMapper();
		// TODO use logger
		System.out.println("SEND -> ");
		String workflowAsString = formParams.get("data").get(0);
		System.out.println(workflowAsString);
		Workflow workflow;
		try {
			workflow = mapper.readValue(workflowAsString, Workflow.class);
		} catch (IOException e) {
			return Response.serverError().entity("Jackson parsing-error").build();
		}
		logic.addWorkflow(workflow);
		return Response.ok().build();
	}
	
	/**
	 * 
	 * @param workflow
	 * @return String true or false
	 */
	@PUT @Path("workflow/{workflowid}")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes("application/x-www-form-urlencoded")
	public Response updateWorkflow(@PathParam("workflowid") int workflowid, MultivaluedMap<String, String> formParams) {
		System.out.println("UPDATE -> " + workflowid);
		ObjectMapper mapper = new ObjectMapper();
		String workflowAsString = formParams.get("data").get(0);
		Workflow workflow;
		try {
			workflow = mapper.readValue(workflowAsString, Workflow.class);
		} catch (IOException e) {
			return Response.serverError().entity("Jackson parsing-error").build();
		}
		logic.addWorkflow(workflow);
		return Response.ok().build();
	}
	
	/**
	 * 
	 * @param workflowid
	 * @return deleted workflow, if successful
	 */
	@DELETE @Path("workflow/{workflowid}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response deleteWorkflow (@PathParam("workflowid") int workflowid) {
		System.out.println("DELETE -> " + workflowid);
		ObjectMapper mapper = new ObjectMapper();
		Workflow workflow = logic.getWorkflow(workflowid);
		logic.deleteWorkflow(workflowid);
		String workflowAsString;
		try {
			workflowAsString = mapper.writeValueAsString(workflow);
		} catch (JsonProcessingException e) {
			return Response.serverError().entity("Jackson parsing-error").build();
		}
		return Response.ok(workflowAsString).build();
	}

}
