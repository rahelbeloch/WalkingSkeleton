package de.hsrm.swt02.restserver.resource;

import java.util.List;
import java.util.logging.Level;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import de.hsrm.swt02.businesslogic.Logic;
import de.hsrm.swt02.businesslogic.LogicResponse;
import de.hsrm.swt02.businesslogic.Message;
import de.hsrm.swt02.businesslogic.exceptions.LogicException;
import de.hsrm.swt02.constructionfactory.ConstructionFactory;
import de.hsrm.swt02.logging.UseLogger;
import de.hsrm.swt02.messaging.ServerPublisher;
import de.hsrm.swt02.messaging.ServerPublisherBrokerException;
import de.hsrm.swt02.model.Item;
import de.hsrm.swt02.model.Workflow;
import de.hsrm.swt02.persistence.exceptions.PersistenceException;
import de.hsrm.swt02.persistence.exceptions.WorkflowNotExistentException;
import de.hsrm.swt02.restserver.exceptions.JacksonException;

/**
 * This class is called if client operates on workflows.
 *
 */
@Path("resource")
public class WorkflowResource {

    public static final ConstructionFactory FACTORY = ConstructionFactory
            .getInstance();
    public static final Logic LOGIC = FACTORY.getLogic();
    public static final ServerPublisher PUBLISHER = FACTORY.getPublisher();
    public static final UseLogger LOGGER = new UseLogger();
    private static final String PREFIX = "[restserver] ";

    /**
     * This method returns a requested workflow.
     * @param workflowid indicates which workflow is requested
     * @return the requested workflow
     */
    @GET
    @Path("workflows/{workflowid}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getWorkflow(@PathParam("workflowid") String workflowid) {
        final String loggingBody = PREFIX + "GET /resource/workflows/" + workflowid;
        LOGGER.log(Level.INFO, loggingBody);
        String workflowAsString;
        Workflow workflow = null;


        try {
            workflow = LOGIC.getWorkflow(workflowid);
            workflow.convertReferencesToIdList();
        } catch (PersistenceException e1) {
            LOGGER.log(Level.WARNING, e1);
            return Response.serverError().entity(String.valueOf(e1.getErrorCode())).build();
        }

        try {
            workflowAsString = JsonParser.marshall(workflow);
            LOGGER.log(Level.FINE, loggingBody + workflowAsString);
        } catch (JacksonException e) {
            LOGGER.log(Level.WARNING, loggingBody + e);
            return Response
                    .serverError()
                    .entity(String.valueOf(e
                            .getErrorCode())).build();
        }
        LOGGER.log(Level.INFO, loggingBody + " Request successful.");
        return Response.ok(workflowAsString).build();
    }

    /**
     * This method returns all startable workflows for a given user.
     * @param username the name of the user for whom the workflows shall be
     *            returned
     * @return all startable workflows for user
     */
    @GET
    @Path("workflows/startables")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getStartablesByUser(@HeaderParam("username") String username) {
        final String loggingBody = PREFIX + "GET /resource/workflows/startables";
        LOGGER.log(Level.INFO, loggingBody);
        List<String> wIdList = null;

        try {
            wIdList = LOGIC.getStartableWorkflowsByUser(username);
        } catch (LogicException e1) {
            LOGGER.log(Level.WARNING, e1);
            return Response.serverError().entity(String.valueOf(e1.getErrorCode())).build();
        }

        String wIdListString;

        try {
            wIdListString = JsonParser.marshall(wIdList);
            LOGGER.log(Level.FINE, loggingBody + wIdListString);
        } catch (JacksonException e) {
            LOGGER.log(Level.WARNING, loggingBody + e);
            return Response
                    .serverError()
                    .entity(String.valueOf(e
                            .getErrorCode())).build();
        }
        LOGGER.log(Level.INFO, loggingBody + " Request successful.");
        return Response.ok(wIdListString).build();
    }

    /**
     * This method returns all startable workflows for a given user.
     * @param username the name of the user for whom the workflows shall be
     *            returned
     * @param workflowid the id of the workflow.
     * @return all startable workflows for user
     */
    @GET
    @Path("items/{workflowid}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getRelevantItemsByUser(
            @HeaderParam("username") String username,
            @PathParam("workflowid") String workflowid)
    {
        final String loggingBody = PREFIX + "GET /resource/items/" + workflowid;
        LOGGER.log(Level.INFO, loggingBody);
        List<Item> itemList = null;
        try {
            itemList = LOGIC.getRelevantItemsByUser(workflowid, username);
        } catch (PersistenceException e1) {
            LOGGER.log(Level.WARNING, e1);
            return Response.serverError().entity(String.valueOf(e1.getErrorCode())).build();
        }

        String itemListString;

        try {
            itemListString = JsonParser.marshall(itemList);
            LOGGER.log(Level.FINE, loggingBody + itemListString);
        } catch (JacksonException e) {
            LOGGER.log(Level.WARNING, loggingBody + e);
            return Response
                    .serverError()
                    .entity(String.valueOf(e
                            .getErrorCode())).build();
        }
        LOGGER.log(Level.INFO, loggingBody + " Request successful.");
        return Response.ok(itemListString).build();
    }

    /**
     * This method returns workflows where a user is involved.
     * @param username indicates which user's workflows are requested
     * @return the requested workflow
     */
    @GET
    @Path("workflows")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getWorkflowsByUser(@HeaderParam("username") String username) {
        final String loggingBody = PREFIX + "GET /resource/workflows";
        LOGGER.log(Level.INFO, loggingBody);
        List<Workflow> wflowList = null;
        if (username.equals("TestAdmin")) {
            try {
                wflowList = LOGIC.getAllWorkflows();
            } catch (WorkflowNotExistentException e) {
                LOGGER.log(Level.WARNING, e);
                return Response.serverError()
                        .entity(String.valueOf(e.getErrorCode())).build();
            }
        } else {
            try {
                wflowList = LOGIC.getAllWorkflowsByUser(username);
                for (Workflow w : wflowList) {
                    w.convertReferencesToIdList();
                }
            } catch (LogicException e) {
                LOGGER.log(Level.WARNING, e);
                return Response.serverError().entity(String.valueOf(e.getErrorCode())).build();
            }

        }
        String wListString;

        try {
            wListString = JsonParser.marshall(wflowList);
            LOGGER.log(Level.FINE, loggingBody + wListString);
        } catch (JacksonException e) {
            LOGGER.log(Level.WARNING, loggingBody + e);
            return Response
                    .serverError()
                    .entity(String.valueOf(e
                            .getErrorCode())).build();
        }
        LOGGER.log(Level.INFO, loggingBody + " Request successful.");
        return Response.ok(wListString).build();
    }

    /**
     * Receives a workflow and stores it into the database. This operation will
     * be published on the message broker.
     * @param formParams wrapper for an sent workflow
     * @return "true" if everything was successful OR "jackson exception" if
     *         serialization crashed
     */
    @POST
    @Path("workflows")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes("application/x-www-form-urlencoded")
    public Response saveWorkflow(MultivaluedMap<String, String> formParams) {
        LogicResponse logicResponse;
        final String loggingBody = PREFIX + "POST /resource/workflows";
        LOGGER.log(Level.INFO, loggingBody);
        final String workflowAsString = formParams.get("data").get(0);
        Workflow workflow = new Workflow();

        try {
            workflow = (Workflow)JsonParser.unmarshall(workflowAsString, workflow);
        } catch (JacksonException e) {
            LOGGER.log(Level.WARNING, loggingBody + e);
            return Response
                    .serverError()
                    .entity(String.valueOf(e
                            .getErrorCode())).build();
        }
        workflow.convertIdListToReferences();
        try {
            logicResponse = LOGIC.addWorkflow(workflow);
        } catch (LogicException e1) {
            LOGGER.log(Level.WARNING, e1);
            return Response.serverError()
                    .entity(String.valueOf(e1.getErrorCode())).build();
        }
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
     * @param workflowid indicates which workflow should be updated
     * @param formParams wrapper for an sent workflow
     * @return String true or false
     */
    @PUT
    @Path("workflows/{workflowid}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes("application/x-www-form-urlencoded")
    public Response updateWorkflow(@PathParam("workflowid") int workflowid,
            MultivaluedMap<String, String> formParams) 
    {
        LogicResponse logicResponse;
        final String loggingBody = PREFIX + "PUT /resource/workflows/" + workflowid;
        LOGGER.log(Level.INFO, loggingBody);
        final String workflowAsString = formParams.get("data").get(0);
        Workflow workflow = new Workflow();

        try {
            workflow = (Workflow)JsonParser.unmarshall(workflowAsString, workflow);
        } catch (JacksonException e) {
            LOGGER.log(Level.WARNING, loggingBody
                    + e);
            return Response
                    .serverError()
                    .entity(String.valueOf(e
                            .getErrorCode())).build();
        }

        try {
            logicResponse = LOGIC.addWorkflow(workflow);
        } catch (LogicException e1) {
            LOGGER.log(Level.WARNING, e1);
            return Response.serverError()
                    .entity(String.valueOf(e1.getErrorCode())).build();
        }

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
     * This method sets a workflow's activity.
     * @param workflowid indicates which workflow's activity should be updated
     * @param state indicates if a workflow should be activated or deactivated
     * @return ok if it worked
     */
    @PUT
    @Path("workflows/{workflowid}/{state}")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes("application/x-www-form-urlencoded")
    public Response updateWorkflowActivity(
            @PathParam("workflowid") String workflowid,
            @PathParam("state") String state) 
    {
        LogicResponse logicResponse = null;
        final String loggingBody = PREFIX + "PUT /resource/workflows/" + workflowid + "/" 
                + state;
        LOGGER.log(Level.INFO, loggingBody);

        try {
            if (state.equals("activate")) {
                logicResponse = LOGIC.activateWorkflow(workflowid);
            } else if (state.equals("deactivate")) {
                logicResponse = LOGIC.deactivateWorkflow(workflowid);
            }
        } catch (PersistenceException e) {
            LOGGER.log(Level.WARNING, e);
            return Response.serverError()
                    .entity(String.valueOf(e.getErrorCode())).build();
        }

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
     * @param workflowid which indicates which workflow should be deleted
     * @return deleted workflow, if successful
     */
    @DELETE
    @Path("workflows/{workflowid}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response deleteWorkflow(@PathParam("workflowid") String workflowid) {
        LogicResponse logicResponse;
        final String loggingBody = PREFIX + "DELETE /resource/workflows/" + workflowid;
        LOGGER.log(Level.INFO, loggingBody);
        Workflow workflow = null;
        String workflowAsString;

        try {
            workflow = LOGIC.getWorkflow(workflowid);
            logicResponse = LOGIC.deleteWorkflow(workflowid);
        } catch (PersistenceException e1) {
            LOGGER.log(Level.WARNING, e1);
            return Response.serverError()
                    .entity(String.valueOf(e1.getErrorCode())).build();
        }
            
        try {
            workflowAsString = JsonParser.marshall(workflow);
        } catch (JacksonException e) {
            LOGGER.log(Level.WARNING, e);
            return Response
                    .serverError()
                    .entity(String.valueOf(e
                            .getErrorCode())).build();
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
