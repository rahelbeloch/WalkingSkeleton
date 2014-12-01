using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CommunicationLib.Model;

namespace CommunicationLib
{
    /// <summary>
    ///  Interface that describes the provided methods of this RestAPI.
    /// </summary>
    interface IRestRequester
    {
        // Ressource-methods

        /// <summary>
        ///     Requests all Objects (Items, Workflows or Users) belonging to the given user.
        /// </summary>
        /// <typeparam name="RootElementList">The list of RootElements</typeparam>
        /// <param name="userName">The users name</param>
        /// <returns>The list with RootElements requested from server</returns>
        IList<RootElement> GetAllObjects<RootElement>(String userName);

        /// <summary>
        ///     Get an object from the server, with HTTP-Method GET.
        ///     Path for this HTTP-Method is always: ressource/<typename>/<id>/
        /// </summary>
        /// <typeparam name="O">Type of the requested object</typeparam>
        /// <param name="id">Id of the requested object</param>
        /// <returns>The requested object</returns>
        O GetObject<O>(int id);

        /// <summary>
        ///     Update an object on the server, with HTTP-Method PUT.
        /// </summary>
        /// <param name="sendObj">The object to update</param>
        /// <returns>If it worked or not</returns>
        Boolean UpdateObject(RootElement sendObj);

        /// <summary>
        ///     Create an object on the server, with HTTP-Method POST.
        /// </summary>
        /// <typeparam name="O">The type of the object to be created</typeparam>
        /// <param name="sendObj">The specified object to create</param>
        /// <returns>True if it worked, false/exception otherwise</returns>
        Boolean PostObject<O>(RootElement sendObj);

        /// <summary>
        ///     Delete an object on the server, with HTTP-Method DEL.
        /// </summary>
        /// <typeparam name="O">Type of the deleted object</typeparam>
        /// <param name="id">Id of the deleted object</param>
        /// <returns>The deleted object</returns>
        O DeleteObject<O>(int id);

        // Command-methods

        /// <summary>
        ///     Does a login access to the server. Path ist always: "/command/user/login"
        /// </summary>
        /// <param name="username">Name of the user</param>
        /// <param name="password">Password of the user</param>
        /// <returns>True if it worked, false otherwhise, or an exception</returns>
        Boolean checkUser(String username, String password);

        /// <summary>
        ///     Sends a request to the server to start a workflow (create an item). Path is always: "/command/workflow/start/{workflowId}/{userName}"
        /// </summary>
        /// <param name="wId">Workflow-Id</param>
        /// <param name="uId">Username</param>
        Boolean StartWorkflow(int wId, string username);

        /// <summary>
        ///     Sends a state change of an action to the server. Path is always:  "/command/workflow/forward/{stepId}/{itemId}/{username}"
        /// </summary>
        /// <param name="stepId">Id of the current step</param>
        /// <param name="itemId">Id of the current item</param>
        /// <param name="uId">Name of the current user</param>
        /// <returns>True if it worked, false/exception otherwise</returns>
        Boolean StepForward(int stepId, int itemId, string username);

    }
}
