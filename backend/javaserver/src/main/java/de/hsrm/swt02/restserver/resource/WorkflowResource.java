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
import de.hsrm.swt02.messaging.ServerPublisherBrokerException;
import de.hsrm.swt02.model.Step;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.exceptions.WorkflowNotExistentException;
import de.hsrm.swt02.restserver.LogicResponse;
import de.hsrm.swt02.restserver.Message;

/**
 * This class is called if client operates on workflows.
 *
 */
@Path("resource")
public class WorkflowResource {

    public static final Logic LOGIC = ConstructionFactory.getLogic();
    public static final ServerPublisher PUBLISHER = ConstructionFactory
            .getPublisher();
    public static final UseLogger LOGGER = new UseLogger();
    LogicResponse logicResponse;

    /**
     * This method returns a requested workflow.
     * @param workflowid indicates which workflow is requested
     * @return the requested workflow
     */
    @GET
    @Path("workflow/{workflowid}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getWorkflow(@PathParam("workflowid") int workflowid) {
        final String loggingBody = "GET -> " + workflowid;
        final ObjectMapper mapper = new ObjectMapper();
        Workflow workflow = null;
        try {
            workflow = LOGIC.getWorkflow(workflowid);
        } catch (WorkflowNotExistentException e1) {
            LOGGER.log(Level.INFO, loggingBody
                    + " Non-existing workflow requested.");
            return Response.serverError().entity("11250").build();
        }
        String workflowAsString;
        try {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            workflowAsString = mapper.writeValueAsString(workflow);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.INFO, loggingBody
                    + " JACKSON parsing error occured.");
            return Response.serverError().entity("11210").build();
        }
        LOGGER.log(Level.INFO, loggingBody + " Request successful.");
        return Response.ok(workflowAsString).build();
    }

    /**
     * This method returns workflows where a user is involved.
     * @param username indicates which user's workflows are requested
     * @return the requested workflow
     */
    @GET
    @Path("workflows/{username}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getAllWorkflows(@PathParam("username") String username) {
        final ObjectMapper mapper = new ObjectMapper();
        final String loggingBody = "GETALL -> " + username;
        List<Workflow> wflowList = null;
        wflowList = LOGIC.getWorkflowsByUser(username);
        String wListString;
        try {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            wListString = mapper.writeValueAsString(wflowList);
            System.out.println(wListString);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.INFO, loggingBody
                    + " JACKSON parsing-error occured.");
            return Response.serverError().entity("11210").build();
        }
        LOGGER.log(Level.INFO, loggingBody + " Request successful.");
        return Response.ok(wListString).build();
    }

    /**
     * 
     * receives a workflow and stores it into the database.
     * 
     * @param formParams wrapper for an sent workflow
     * @return "true" if everything was successful OR "jackson exception" if
     *         serialization crashed
     */
    @POST
    @Path("workflow")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes("application/x-www-form-urlencoded")
    public Response saveWorkflow(MultivaluedMap<String, String> formParams) {
        final ObjectMapper mapper = new ObjectMapper();
        final String loggingBody = "SEND -> ";
        final String workflowAsString = formParams.get("data").get(0);
        Workflow workflow = null;
        try {
            workflow = mapper.readValue(workflowAsString, Workflow.class);
        } catch (IOException e) {
            LOGGER.log(Level.INFO, loggingBody + " JACKSON parsing-error occured.");
            return Response.serverError().entity("11210").build();
        }

        convertIdListToReferences(workflow);

        logicResponse = LOGIC.addWorkflow(workflow);
        for (Message m : logicResponse.getMessages()) {
            try {
                PUBLISHER.publish(m.getValue(), m.getTopic());
            } catch (ServerPublisherBrokerException e) {
                LOGGER.log(Level.WARNING, "Publisher not responding!");
            }
        }
        LOGGER.log(Level.INFO, loggingBody + " Workflow successfully saved.");
        return Response.ok().build();
    }

    /**
     * incoming order of step ids are converted into references.
     * 
     * @param workflow which is operated on
     */
    private void convertIdListToReferences(Workflow workflow) {
        for (Step step : workflow.getSteps()) {
            for (int id : step.getNextStepIds()) {
                step.getNextSteps().add(workflow.getStepById(id));
            }
        }
    }

    /**
     * This method updates a workflow.
     * @param workflowid indicates which workflow should be updated
     * @param formParams wrapper for an sent workflow
     * @return String true or false
     */
    @PUT
    @Path("workflow/{workflowid}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes("application/x-www-form-urlencoded")
    public Response updateWorkflow(@PathParam("workflowid") int workflowid,
            MultivaluedMap<String, String> formParams) 
    {
        final String loggingBody = "UPDATE -> " + workflowid;
        final ObjectMapper mapper = new ObjectMapper();
        final String workflowAsString = formParams.get("data").get(0);
        Workflow workflow;
        try {
            workflow = mapper.readValue(workflowAsString, Workflow.class);
        } catch (IOException e) {
            LOGGER.log(Level.INFO, loggingBody
                    + " JACKSON parsing-error occured.");
            return Response.serverError().entity("11210").build();
        }
        logicResponse = LOGIC.addWorkflow(workflow);
        for (Message m : logicResponse.getMessages()) {
            try {
                PUBLISHER.publish(m.getValue(), m.getTopic());
            } catch (ServerPublisherBrokerException e) {
                LOGGER.log(Level.WARNING, "Publisher not responding!");
            }
        }
        LOGGER.log(Level.INFO, loggingBody + " Workflow successfully updated.");
        return Response.ok().build();
    }

    /**
     * This method deletes a workflow.
     * @param workflowid which indicates which workflow should be deleted
     * @return deleted workflow, if successful
     */
    @DELETE
    @Path("workflow/{workflowid}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteWorkflow(@PathParam("workflowid") int workflowid) {
        final String loggingBody = "DELETE -> " + workflowid;
        final ObjectMapper mapper = new ObjectMapper();
        Workflow workflow = null;
        try {
            workflow = LOGIC.getWorkflow(workflowid);
            LOGIC.deleteWorkflow(workflowid);
        } catch (WorkflowNotExistentException e1) {
            LOGGER.log(Level.INFO, loggingBody + " Workflow does not exist.");
            return Response.serverError().entity("11250").build();
        }
        String workflowAsString;
        try {
            workflowAsString = mapper.writeValueAsString(workflow);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.INFO, loggingBody
                    + " JACKSON parsing-error occured.");
            return Response.serverError().entity("11210").build();
        }
        LOGGER.log(Level.INFO, loggingBody + " Workflow succesfully deleted.");
        return Response.ok(workflowAsString).build();
    }

}
