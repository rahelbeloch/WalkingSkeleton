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
import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.constructionfactory.ConstructionFactory;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.messaging.ServerPublisher;
import de.hsrm.swt02.messaging.ServerPublisherBrokerException;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.exceptions.UserNotExistentException;
import de.hsrm.swt02.persistence.exceptions.WorkflowNotExistentException;
import de.hsrm.swt02.restserver.LogicResponse;
import de.hsrm.swt02.restserver.Message;
import de.hsrm.swt02.restserver.exceptions.JacksonException;

/**
 * This class is called if client operates on workflows.
 *
 */
@Path("resource")
public class WorkflowResource {
    
    public static final ConstructionFactory FACTORY = ConstructionFactory.getInstance();
    public static final Logic LOGIC = FACTORY.getLogic();
    public static final ServerPublisher PUBLISHER = FACTORY.getPublisher();
    public static final UseLogger LOGGER = new UseLogger();
    LogicResponse logicResponse;

    /**
     * This method returns a requested workflow.
     * 
     * @param workflowid indicates which workflow is requested
     * @return the requested workflow
     */
    @GET
    @Path("workflow/{workflowid}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getWorkflow(@PathParam("workflowid") int workflowid) {
        final String loggingBody = "GET -> " + workflowid;
        final ObjectMapper mapper = new ObjectMapper();
        String workflowAsString;
        Workflow workflow = null;

        try {
            workflow = LOGIC.getWorkflow(workflowid);
            workflow.convertReferencesToIdList();
        } catch (WorkflowNotExistentException e1) {
            LOGGER.log(Level.WARNING, loggingBody
                    + " Non-existing workflow requested.");
            return Response.serverError().entity(String.valueOf(e1.getErrorCode())).build();
        }
        try {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            workflowAsString = mapper.writeValueAsString(workflow);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.WARNING, loggingBody + "Jackson parsing-error");
            return Response.serverError()
                    .entity(String.valueOf(new JacksonException().getErrorCode())).build();
        }
        LOGGER.log(Level.INFO, loggingBody + " Request successful.");
        return Response.ok(workflowAsString).build();
    }

    /**
     * This method returns all workflows.
     * 
     * @return all workflows
     */
    @GET
    @Path("workflows")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getAllWorkflows() {
        final ObjectMapper mapper = new ObjectMapper();
        final String loggingBody = "GETALL";
        List<Workflow> wflowList = null;
        try {
            wflowList = LOGIC.getAllWorkflows();
            for(Workflow w : wflowList) {
                w.convertReferencesToIdList();
            }
        } catch (WorkflowNotExistentException e1) {
            LOGGER.log(Level.WARNING, e1);
            return Response.serverError().entity(String.valueOf(e1.getErrorCode())).build();
        }
        String wListString;

        try {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            wListString = mapper.writeValueAsString(wflowList);
            LOGGER.log(Level.FINE, loggingBody + wListString);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.WARNING, loggingBody + " Jackson parsing-error");
            return Response.serverError()
                    .entity(String.valueOf(new JacksonException().getErrorCode())).build();
        }
        LOGGER.log(Level.INFO, loggingBody + " Request successful.");
        return Response.ok(wListString).build();
    }
    
    /**
     * This method returns all startable workflows for a given user.
     * 
     * @param username the name of the user for whom the workflows shall be returned
     * @return all startable workflows for user
     */
    @GET
    @Path("workflows/startables/{username}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getStartablesByUser(@PathParam("username") String username) {
        final ObjectMapper mapper = new ObjectMapper();
        final String loggingBody = "GETSTARTABLES -> " + username;
        List<Integer> wIdList = null;
        try {
            wIdList = LOGIC.getStartableWorkflowsByUser(username);
        } catch (WorkflowNotExistentException e1) {
            LOGGER.log(Level.WARNING, e1);
            return Response.serverError().entity(String.valueOf(e1.getErrorCode())).build();
        } catch (UserNotExistentException e2) {
            LOGGER.log(Level.WARNING, e2);
            return Response.serverError().entity(String.valueOf(e2.getErrorCode())).build();
        } catch (LogicException e3) {
            LOGGER.log(Level.WARNING, e3);
            return Response.serverError().entity(String.valueOf(e3.getErrorCode())).build();
        }
        String wIdListString;

        try {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            wIdListString = mapper.writeValueAsString(wIdList);
            LOGGER.log(Level.FINE, loggingBody + wIdListString);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.WARNING, loggingBody + " Jackson parsing-error");
            return Response.serverError()
                    .entity(String.valueOf(new JacksonException().getErrorCode())).build();
        }
        LOGGER.log(Level.INFO, loggingBody + " Request successful.");
        return Response.ok(wIdListString).build();
    }
    
    /**
     * This method returns all startable workflows for a given user.
     * 
     * @param username the name of the user for whom the workflows shall be returned
     * @return all startable workflows for user
     */
    @GET
    @Path("items/{username}/{workflowid}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getRelevantItemsByUser(@PathParam("username") String username,@PathParam("workflowid") int workflowid) {
        final ObjectMapper mapper = new ObjectMapper();
        final String loggingBody = "GETITEMS -> " + username;
        List<Item> itemList = null;
        try {
            itemList = LOGIC.getRelevantItemsByUser(workflowid, username);
        } catch (WorkflowNotExistentException e1) {
            LOGGER.log(Level.WARNING, e1);
            return Response.serverError().entity(String.valueOf(e1.getErrorCode())).build();
        } catch (UserNotExistentException e) {
            LOGGER.log(Level.WARNING, e);
            return Response.serverError().entity(String.valueOf(e.getErrorCode())).build();
        }
        String itemListString;

        try {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            itemListString = mapper.writeValueAsString(itemList);
            LOGGER.log(Level.FINE, loggingBody + itemListString);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.WARNING, loggingBody + " Jackson parsing-error");
            return Response.serverError()
                    .entity(String.valueOf(new JacksonException().getErrorCode())).build();
        }
        LOGGER.log(Level.INFO, loggingBody + " Request successful.");
        return Response.ok(itemListString).build();
    }
    
    /**
     * This method returns workflows where a user is involved.
     * 
     * @param username indicates which user's workflows are requested
     * @return the requested workflow
     */
    @GET
    @Path("workflows/{username}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getWorkflowsByUser(@PathParam("username") String username) {
        final ObjectMapper mapper = new ObjectMapper();
        final String loggingBody = "GETALL -> " + username;
        List<Workflow> wflowList = null;
        try {
            wflowList = LOGIC.getAllWorkflowsByUser(username);
            for(Workflow w : wflowList) {
                w.convertReferencesToIdList();
            }
        } catch (WorkflowNotExistentException e1) {
            LOGGER.log(Level.WARNING, e1);
            return Response.serverError().entity(String.valueOf(e1.getErrorCode())).build();
        } catch (UserNotExistentException e2) {
            LOGGER.log(Level.WARNING, e2);
            return Response.serverError().entity(String.valueOf(e2.getErrorCode())).build();
        } catch (LogicException e3) {
            LOGGER.log(Level.WARNING, e3);
            return Response.serverError().entity(String.valueOf(e3.getErrorCode())).build();
        }
        String wListString;

        try {
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            wListString = mapper.writeValueAsString(wflowList);
            LOGGER.log(Level.FINE, loggingBody + wListString);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.WARNING, loggingBody + "Jackson parsing-error");
            return Response.serverError()
                    .entity(String.valueOf(new JacksonException().getErrorCode())).build();
        }
        LOGGER.log(Level.INFO, loggingBody + " Request successful.");
        return Response.ok(wListString).build();
    }

    /**
     * Receives a workflow and stores it into the database. This operation will
     * be published on the message broker.
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
            LOGGER.log(Level.WARNING, loggingBody + "Jackson parsing-error");
            return Response.serverError()
                    .entity(String.valueOf(new JacksonException().getErrorCode())).build();
        }
        workflow.convertIdListToReferences();
        logicResponse = LOGIC.addWorkflow(workflow);
        for (Message m : logicResponse.getMessages()) {
            try {
                PUBLISHER.publish(m.getValue(), m.getTopic());
            } catch (ServerPublisherBrokerException e) {
                LOGGER.log(Level.WARNING, e);
            }
        }
        LOGGER.log(Level.INFO, loggingBody + " Workflow successfully saved.");
        return Response.ok().build();
    }

    /**
     * This method updates a workflow. This operation will be published on the
     * message broker.
     * 
     * @param workflowid indicates which workflow should be updated
     * @param formParams wrapper for an sent workflow
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
            LOGGER.log(Level.WARNING, loggingBody
                    + " JACKSON parsing-error occured.");
            return Response.serverError()
                    .entity(String.valueOf(new JacksonException().getErrorCode())).build();
        }
        logicResponse = LOGIC.addWorkflow(workflow);
        for (Message m : logicResponse.getMessages()) {
            try {
                PUBLISHER.publish(m.getValue(), m.getTopic());
            } catch (ServerPublisherBrokerException e) {
                LOGGER.log(Level.WARNING, e);
            }
        }
        LOGGER.log(Level.INFO, loggingBody + " Workflow successfully updated.");
        return Response.ok().build();
    }

    /**
     * This method deletes a workflow. This operation will be published on the
     * message broker.
     * 
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
        String workflowAsString;

        try {
            workflow = LOGIC.getWorkflow(workflowid);
            logicResponse = LOGIC.deleteWorkflow(workflowid);
        } catch (WorkflowNotExistentException e1) {
            LOGGER.log(Level.WARNING, loggingBody + e1);
            return Response.serverError().entity(String.valueOf(e1.getErrorCode())).build();
        }
        try {
            workflowAsString = mapper.writeValueAsString(workflow);
        } catch (JsonProcessingException e) {
            LOGGER.log(Level.WARNING, loggingBody + e);
            return Response.serverError()
                    .entity(String.valueOf(new JacksonException().getErrorCode())).build();
        }
        for (Message m : logicResponse.getMessages()) {
            try {
                PUBLISHER.publish(m.getValue(), m.getTopic());
            } catch (ServerPublisherBrokerException e) {
                LOGGER.log(Level.WARNING, e);
            }
        }
        LOGGER.log(Level.INFO, loggingBody + " Workflow succesfully deleted.");
        return Response.ok(workflowAsString).build();
    }
}
