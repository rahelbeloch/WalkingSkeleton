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

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.constructionfactory.ConstructionFactory;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.messaging.ServerPublisher;
import de.hsrm.swt02.messaging.ServerPublisherBrokerException;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.exceptions.WorkflowNotExistentException;
import de.hsrm.swt02.restserver.LogicResponse;
import de.hsrm.swt02.restserver.Message;

@Path("resource")
public class WorkflowResource {

    public static final Logic LOGIC = ConstructionFactory.getLogic();
    public static final ServerPublisher PUBLISHER = ConstructionFactory
            .getPublisher();
    public static final UseLogger LOGGER = new UseLogger();
    LogicResponse logicResponse = new LogicResponse();

    /**
     * 
     * @param workflowid indicates which workflow is looked for
     * @return the requested workflow
     */
    @GET
    @Path("workflow/{workflowid}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getWorkflow(@PathParam("workflowid") int workflowid) {
        final ObjectMapper mapper = new ObjectMapper();
        Workflow workflow = null;
        final String loggingBody = "GETWORKFLOW -> " + workflowid;
        try {
            workflow = LOGIC.getWorkflow(workflowid);
        } catch (WorkflowNotExistentException e1) {
            LOGGER.log(Level.INFO, loggingBody
                    + " Non-existing workflow requested.");
            return Response.serverError().entity("Workflow does not exist.")
                    .build();
        }
        String workflowAsString;
        try {
            workflowAsString = mapper.writeValueAsString(workflow);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.INFO, loggingBody
                    + " JACKSON parsing error occured.");
            return Response.serverError().entity("JACKSON parsing-error")
                    .build();
        }
        LOGGER.log(Level.INFO, loggingBody + " Request successful.");
        return Response.ok(workflowAsString).build();
    }

    /**
     * 
     * @param username indicates the user whose workflows are asked for
     * @return the requested workflow
     */
    @GET
    @Path("workflows/{username}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getAllWorkflows(@PathParam("username") String username) {
        final ObjectMapper mapper = new ObjectMapper();
        final String loggingBody = "GETALLWORKFLOWS -> " + username;
        List<Workflow> wflowList = null;
        wflowList = LOGIC.getWorkflowsByUser(username);
        String wListString;
        try {
            wListString = mapper.writeValueAsString(wflowList);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.INFO, loggingBody
                    + " JACKSON parsing-error occured.");
            return Response.serverError().entity("JACKSON parsing-error")
                    .build();
        }
        LOGGER.log(Level.INFO, loggingBody + " Request successful.");
        return Response.ok(wListString).build();
    }

    /**
     * 
     * receives a workflow and stores it into the database.
     * 
     * @param receivedWorkflow
     * @return "true" if everything was successful OR "jackson exception" if
     *         serialization crashed
     */
    @POST
    @Path("workflow")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes("application/x-www-form-urlencoded")
    public Response saveWorkflow(MultivaluedMap<String, String> formParams) {
        final ObjectMapper mapper = new ObjectMapper();
        final String workflowAsString = formParams.get("data").get(0);
        final String loggingBody = "SENDWORKFLOW";
        Workflow workflow;
        try {
            workflow = mapper.readValue(workflowAsString, Workflow.class);
        } catch (IOException e) {
            LOGGER.log(Level.INFO, loggingBody
                    + " JACKSON parsing-error occured.");
            return Response.serverError().entity("JACKSON parsing-error")
                    .build();
        }
        logicResponse = LOGIC.addWorkflow(workflow);
        for (Message m : logicResponse.getMessages()) {
            try {
                PUBLISHER.publish(m.getValue(), m.getTopic());
            } catch (ServerPublisherBrokerException e) {
                // TODO Logging
            }
        }
        LOGGER.log(Level.INFO, loggingBody + " Workflow successfully stored.");
        return Response.ok().build();
    }

    /**
     * 
     * @param workflow
     * @return String true or false
     */
    @PUT
    @Path("workflow/{workflowid}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes("application/x-www-form-urlencoded")
    public Response updateWorkflow(@PathParam("workflowid") int workflowid,
            MultivaluedMap<String, String> formParams) {
        final String loggingBody = "UPDATE -> " + workflowid;
        final ObjectMapper mapper = new ObjectMapper();
        final String workflowAsString = formParams.get("data").get(0);
        Workflow workflow;
        try {
            workflow = mapper.readValue(workflowAsString, Workflow.class);
        } catch (IOException e) {
            LOGGER.log(Level.INFO, loggingBody
                    + " JACKSON parsing-error occured.");
            return Response.serverError().entity("JACKSON parsing-error")
                    .build();
        }
        logicResponse = LOGIC.addWorkflow(workflow);
        for (Message m : logicResponse.getMessages()) {
            try {
                PUBLISHER.publish(m.getValue(), m.getTopic());
            } catch (ServerPublisherBrokerException e) {
                // TODO Logging
            }
        }
        LOGGER.log(Level.INFO, loggingBody + " Workflow successfully updated.");
        return Response.ok().build();
    }

    /**
     * 
     * @param workflowid
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
            logicResponse = LOGIC.deleteWorkflow(workflowid);
        } catch (WorkflowNotExistentException e1) {
            LOGGER.log(Level.INFO, loggingBody + " Workflow does not exist.");
            return Response.serverError().entity("Workflow does not exist")
                    .build();
        }
        String workflowAsString;
        try {
            workflowAsString = mapper.writeValueAsString(workflow);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.INFO, loggingBody
                    + " JACKSON parsing-error occured.");
            return Response.serverError().entity("JACKSON parsing-error")
                    .build();
        }
        for (Message m : logicResponse.getMessages()) {
            try {
                PUBLISHER.publish(m.getValue(), m.getTopic());
            } catch (ServerPublisherBrokerException e) {
                // TODO Logging
            }
        }
        LOGGER.log(Level.INFO, loggingBody + " Workflow successfully deleted.");
        return Response.ok(workflowAsString).build();
    }

}
