package de.hsrm.swt02.restserver.resource;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

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
import com.fasterxml.jackson.databind.SerializationFeature;

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.constructionfactory.ConstructionFactory;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.messaging.ServerPublisher;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.exceptions.WorkflowNotExistentException;

@Path("resource")
public class WorkflowResource {

    public static final Logic logic = ConstructionFactory.getLogic();
    public static final ServerPublisher publisher = ConstructionFactory.getPublisher();
    public static final UseLogger logger = new UseLogger();
    
	/**
	 * 
	 * @param workflowid
	 * @return the requested workflow
	 */	@GET @Path("workflow/{workflowid}")
	@Produces(MediaType.TEXT_PLAIN)
	public Response getWorkflow (@PathParam("workflowid") int workflowid) {
		String loggingBody = "GET -> " + workflowid;
		ObjectMapper mapper = new ObjectMapper();
		Workflow workflow = null;
        try {
            workflow = logic.getWorkflow(workflowid);
        } catch (WorkflowNotExistentException e1) {
            logger.log(Level.INFO, loggingBody + " Non-existing workflow requested.");
            return Response.serverError().entity("11250").build();
        }
		String workflowAsString;
		try {
		    mapper.enable(SerializationFeature.INDENT_OUTPUT);
		    workflowAsString = mapper.writeValueAsString(workflow);
		} catch (JsonProcessingException e) {
			logger.log(Level.INFO, loggingBody + " JACKSON parsing error occured.");
			return Response.serverError().entity("11210").build();
		}
		logger.log(Level.INFO, loggingBody + " Request successful.");
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
		String loggingBody = "GETALL -> " + username;
		List<Workflow> wflowList = null;
        wflowList = logic.getWorkflowsByUser(username);
		String wListString;
		try {
			wListString = mapper.writeValueAsString(wflowList);
		} catch (JsonProcessingException e) {
			logger.log(Level.INFO, loggingBody + " JACKSON parsing-error occured.");
			return Response.serverError().entity("11210").build();
		}
		logger.log(Level.INFO, loggingBody + " Request successful.");
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
		String loggingBody = "SEND -> ";
		String workflowAsString = formParams.get("data").get(0);
		System.out.println(workflowAsString);
		
		Workflow workflow = null;
		try {
		    workflow = mapper.readValue(workflowAsString, Workflow.class);
		} catch (IOException e) {
			logger.log(Level.INFO, loggingBody + " JACKSON parsing-error occured.");
		    return Response.serverError().entity("11210").build();
		}
		logic.addWorkflow(workflow);
		logger.log(Level.INFO, loggingBody + " Workflow successfully saved.");
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
		String loggingBody = "UPDATE -> " + workflowid;
		ObjectMapper mapper = new ObjectMapper();
		String workflowAsString = formParams.get("data").get(0);
		Workflow workflow;
		try {
			workflow = mapper.readValue(workflowAsString, Workflow.class);
		} catch (IOException e) {
			logger.log(Level.INFO, loggingBody + " JACKSON parsing-error occured.");
			return Response.serverError().entity("11210").build();
		}
		logic.addWorkflow(workflow);
		logger.log(Level.INFO, loggingBody + " Workflow successfully updated.");
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
		String loggingBody = "DELETE -> " + workflowid;
		ObjectMapper mapper = new ObjectMapper();
		Workflow workflow = null;
        try {
            workflow = logic.getWorkflow(workflowid);
            logic.deleteWorkflow(workflowid);
        } catch (WorkflowNotExistentException e1) {
        	logger.log(Level.INFO, loggingBody + " Workflow does not exist.");
            return Response.serverError().entity("11250").build();
        }
		String workflowAsString;
		try {	
			workflowAsString = mapper.writeValueAsString(workflow);
		} catch (JsonProcessingException e) {
			logger.log(Level.INFO, loggingBody + " JACKSON parsing-error occured.");
			return Response.serverError().entity("11210").build();
		}
		logger.log(Level.INFO, loggingBody + " Workflow succesfully deleted.");
		return Response.ok(workflowAsString).build();
	}

}
