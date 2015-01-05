using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CommunicationLib.Model;
using System.Security;

namespace CommunicationLib
{
    /// <summary>
    ///  Interface that describes the provided methods of this RestAPI.
    /// </summary>
    public interface IRestRequester
    {
        // Initialize Method
        void InitializeClientProperties(string username, SecureString password);
        void DeleteClientProperties();

        // RESSOURCE-METHODS - get, post, update, delete - do something on the ressources

        /// <summary>
        ///  Method to retrieve all existent objects of given element on server.
        /// </summary>
        /// <returns>List of all elements</returns>
        IList<O> GetAllElements<O>();

        /// <summary>
        ///  Method to retrieve all existent workflows of one given user on server.
        /// </summary>
        /// <param name="username">Requested username</param>
        /// <returns>List of all workflow of this user</returns>
        IList<Workflow> GetAllWorkflowsByUser();

        /// <summary>
        ///  Method to retrieve all startable workflows of one given user. 
        /// </summary>
        /// <param name="username">Requested username</param>
        /// <returns>List of all startable workflows of this user</returns>
        IList<string> GetStartablesByUser();

        /// <summary>
        ///  Method to retrieve all relevant items of one given user. Relevant means all items where the user can accept or close actions.
        /// </summary>
        /// <param name="workflowID">The actual handled workflow</param>
        /// <param name="username">The requested user</param>
        /// <returns>List of relevant items</returns>
        IList<Item> GetRelevantItemsByUser(string workflowID);

        /// <summary>
        ///     Get an object from the server, with HTTP-Method GET.
        ///     Path for this HTTP-Method is always: ressource/<typename>/<id>/
        /// </summary>
        /// <typeparam name="O">Type of the requested object</typeparam>
        /// <param name="id">Id of the requested object</param>
        /// <returns>The requested object</returns>
        O GetObject<O>(string id) where O : new();

        /// <summary>
        ///     Update an object on the server, with HTTP-Method PUT. Path if sendObj is Workflow or Item: 'resource/<typename>/<id>', if user:  'resource/<typename>/<username>'
        /// </summary>
        /// <param name="sendObj">The object to update</param>
        /// <returns>If it worked or not</returns>
        Boolean UpdateObject(RootElement sendObj);

        /// <summary>
        ///     Create an object on the server, with HTTP-Method POST. Path is: 'resource/<typename>'
        /// </summary>
        /// <typeparam name="O">The type of the object to be created</typeparam>
        /// <param name="sendObj">The specified object to create</param>
        /// <returns>True if it worked, false/exception otherwise</returns>
        Boolean PostObject(RootElement sendObj);

        /// <summary>
        ///  Delete an object (Item or Workflow) on the server, with HTTP-Method DEL. Path is: 'resource/<typename>/<resId>'
        /// </summary>
        /// <typeparam name="O">Type of the delete object</typeparam>
        /// <param name="id">ID of the object to delete</param>
        /// <returns>The deleted Object</returns>
        O DeleteObject<O>(string id) where O : new();

        // COMMAND-METHODS

        /// <summary>
        ///     Does a login access to the server. Path ist always: '/command/user/login'
        /// </summary>
        /// <param name="username">Name of the user</param>
        /// <param name="password">Password of the user</param>
        /// <returns>True if it worked, false otherwhise, or an exception</returns>
        Boolean checkUser(String username, SecureString password);

        /// <summary>
        ///     Sends a request to the server to start a workflow (create an item). Path is always: '/command/workflow/start/{workflowId}/{userName}'
        /// </summary>
        /// <param name="wId">Workflow-Id</param>
        /// <param name="uId">Username</param>
        Boolean StartWorkflow(string wId);

        /// <summary>
        ///     Sends a state change of an action to the server. Path is always:  '/command/workflow/forward/{stepId}/{itemId}/{username}'
        /// </summary>
        /// <param name="stepId">Id of the current step</param>
        /// <param name="itemId">Id of the current item</param>
        /// <param name="uId">Name of the current user</param>
        /// <returns>True if it worked, false/exception otherwise</returns>
        Boolean StepForward(string stepId, string itemId);
    }
}