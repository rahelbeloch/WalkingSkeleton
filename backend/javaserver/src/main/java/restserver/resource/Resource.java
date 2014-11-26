package restserver.resource;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import abstractbeans.AbstractWorkflow;

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
		AbstractWorkflow workflow = new AbstractWorkflow();
		workflow.setId(workflowid);
		return "true";
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
	 * @return true or false as String
	 */
	@POST @Path("workflow")
	@Produces(MediaType.TEXT_PLAIN)
	@Consumes("application/x-www-form-urlencoded")
	public String saveWorkflow (MultivaluedMap<String, String> formParams) {
		ObjectMapper mapper = new ObjectMapper();
		System.out.println("SEND -> ");
		String workflowAsString = formParams.get("data").get(0);
		Workflow workflow;
		workflow = mapper.readValue(workflowAsString, Workflow.class);
		return "true";
	}
	
	/**
	 * 
	 * @param workflow
	 * @return String true or false
	 */
	@PUT @Path("workflow/{workflowid}")
	@Produces(MediaType.TEXT_PLAIN)
	public String updateWorkflow(@PathParam("workflowid") int workflowid, String param) {
		System.out.println("UPDATE -> " + workflowid + " " + param);
		return "true";
	}
	
	/**
	 * 
	 * @param workflowid
	 * @return deleted workflow, if successful
	 */
	@DELETE @Path("workflow/{workflowid}")
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteWorkflow (@PathParam("workflowid") int workflowid, String param) {
		System.out.println("DELETE -> " + workflowid + " " + param);
		return "true";
	}
	
}
