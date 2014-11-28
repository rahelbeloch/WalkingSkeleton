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

import de.hsrm.swt02.model.RootElementList;
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
		//TODO: get workflow with id "workflowid" from persistence
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
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("GETALL -> " + username);
		//TODO: get all workflows for user "username" from persistence
		Workflow w1 = new Workflow();
		Workflow w2 = new Workflow();
		Workflow w3 = new Workflow();
		w1.setId(1);
		w2.setId(2);
		w3.setId(3);
		RootElementList wList = new RootElementList();
		wList.add(w1);
		wList.add(w2);
		wList.add(w3);
		String wListString;
		try {
			wListString = mapper.writeValueAsString(wList);
		} catch (JsonProcessingException e) {
			return "jackson exception";
		}
		return wListString;
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
		Workflow workflow;
		try {
			workflow = mapper.readValue(workflowAsString, Workflow.class);
		} catch (IOException e) {
			return "jackson exception";
		}
		//TODO: save Workflow "workflow" to persistence
		System.out.println(workflowAsString);
		System.out.println(workflow.getId());
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
		//TODO: update Workflow "workflow" persistence
		System.out.println(workflowAsString);
		System.out.println(workflow.getId());
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
		//TODO: get workflow with id "workflowid" from persistence, then delete it
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
