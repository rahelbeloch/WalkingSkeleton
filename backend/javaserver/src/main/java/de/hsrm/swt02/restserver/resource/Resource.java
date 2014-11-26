package de.hsrm.swt02.restserver.resource;

import java.io.IOException;

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

import de.hsrm.swt02.model.Workflow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Path("resource")
public class Resource {

	/**
	 * 
	 * @param workflowid
	 * @return the requested workflow
	 */	@GET @Path("workflow/{workflowid}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getWorkflow (@PathParam("workflowid") int workflowid) {
		System.out.println("GET -> " + workflowid);

		ObjectMapper mapper = new ObjectMapper();
		Workflow workflow = new Workflow();
		workflow.setId(workflowid);
		String workflowAsString = "string";
		try {
			workflowAsString = mapper.writeValueAsString(workflow);
		} catch (JsonProcessingException e) {
			return "jackson exception";
		}
		return workflowAsString;
	}
	
	/**
	 * 
	 * @param workflowid
	 * @return the requested workflow
	 */
	@GET @Path("workflows/{username}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getAllWorkflows (@PathParam("username") String username) {
		System.out.println("GETALL -> " + username);
		return "alle workflows für " + username;
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
	public String saveWorkflow (MultivaluedMap<String, String> formParams) {
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("SEND -> ");
		String workflowAsString = formParams.get("data").get(0);
		Workflow workflow; // TODO: connect to business logic
		try {
			workflow = mapper.readValue(workflowAsString, Workflow.class);
		} catch (IOException e) {
			return "jackson exception";
		}
		return "true";
	}
	
	/**
	 * 
	 * @param workflow
	 * @return String true or false
	 */
	@PUT @Path("workflow/{workflowid}")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes("application/x-www-form-urlencoded")
	public String updateWorkflow(@PathParam("workflowid") int workflowid, MultivaluedMap<String, String> formParams) {
		System.out.println("UPDATE -> " + workflowid);
		ObjectMapper mapper = new ObjectMapper();
		String workflowAsString = formParams.get("data").get(0);
		Workflow workflow;
		try {
			workflow = mapper.readValue(workflowAsString, Workflow.class);
		} catch (IOException e) {
			return "jackson exception";
		}
		return "true";
	}
	
	/**
	 * 
	 * @param workflowid
	 * @return deleted workflow, if successful
	 */
	@DELETE @Path("workflow/{workflowid}")
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteWorkflow (@PathParam("workflowid") int workflowid) {
		System.out.println("DELETE -> " + workflowid);
		ObjectMapper mapper = new ObjectMapper();
		Workflow workflow = new Workflow();
		workflow.setId(workflowid);
		String workflowAsString = "string";
		try {
			workflowAsString = mapper.writeValueAsString(workflow);
		} catch (JsonProcessingException e) {
			return "jackson exception";
		}
		return workflowAsString;
	}
	
}
