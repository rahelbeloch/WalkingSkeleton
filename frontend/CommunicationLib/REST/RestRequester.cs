using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using RestSharp;
using System.Web;
using CommunicationLib.Model;
using CommunicationLib.Exception;
using System.Collections;
using System.Reflection;
using Newtonsoft.Json;
using System.Configuration;
using System.Net;
using System.Security;
using CommunicationLib;

namespace RestAPI
{
    /// <summary>
    /// Class that provides the communication to a server via HTTP (POST, PUT, DELETE, GET).
    /// </summary>
    public class RestRequester : CommunicationLib.IRestRequester
    {

        private static JsonSerializerSettings _jsonSettings;

        private string _myUsername;
        private SecureString _myPassword;

        /// <summary>
        /// Default constructor, initializes the serialization settings and pre-strings for urls.
        /// </summary>
        public RestRequester()
        {
            _jsonSettings = new JsonSerializerSettings
            {
                TypeNameHandling = TypeNameHandling.Auto,
                Formatting = Formatting.Indented,
                Binder = new CustomSerializationBinder()
            };
        }

        /// <summary>
        /// Set client properties in rest-interface.
        /// </summary>
        /// <param name="username">username of logged user in client</param>
        /// <param name="password">the password</param>
        public void InitializeClientProperties(string username, SecureString password)
        {
            _myUsername = username;
            _myPassword = password;
        }

        /// <summary>
        /// Delete client properties in rest-interface.
        /// </summary>
        public void DeleteClientProperties()
        {
            _myUsername = null;
            _myPassword = null;
        }


        public IList<O> GetAllElements<O>()
        {
            String url = URLRouter.generateUrl(UrlMethod.Resource, typeof(O));
            
            IList<O> eleList;
            try
            {
                eleList = GetElementList<O>(url);
            }
            catch (BasicException)
            {
                throw;
            }
            return eleList;
        }

        /// <summary>
        ///  Method to retrieve all existent workflows of one given user on server.
        /// </summary>
        /// <param name="username">Requested username</param>
        /// <returns>List of all workflow of this user</returns>
        public IList<Workflow> GetAllWorkflowsByUser()
        {
            String url = URLRouter.generateUrl(UrlMethod.Resource, typeof(Workflow));
            
            IList<Workflow> eleList;
            try
            {
                eleList = GetElementList<Workflow>(url);
            }
            catch (BasicException)
            {
                throw;
            } 
            return eleList;
        }

        /// <summary>
        ///  Method to retrieve all startable workflows of one given user. 
        /// </summary>
        /// <param name="username">Requested username</param>
        /// <returns>List of all startable workflows of this user</returns>
        public IList<string> GetStartablesByUser()
        {
            IRestResponse response;
            String url = URLRouter.generateUrl(UrlMethod.Resource, typeof(Workflow), new string[] { });
            
            var request = createRequest(url, Method.GET);
            request.AddParameter("state", "startable", ParameterType.HttpHeader);

            try
            {
                // call Internal Requester to finally send the request
                response = InternalRequester.RetrieveRequest(request);
            }
            catch (BasicException)
            {
                throw;
            }

            // Deserialization
            IList<string> eleList = JsonConvert.DeserializeObject<List<string>>(response.Content, _jsonSettings);
            
            return eleList;
        }

        /// <summary>
        ///  Method to retrieve all relevant items of one given user. Relevant means all items where the user can accept or close actions.
        /// </summary>
        /// <param name="workflowID">The actual handled workflow</param>
        /// <param name="username">The requested user</param>
        /// <returns>List of relevant items</returns>
        public IList<Item> GetRelevantItemsByUser(string workflowID)
        {
            String url = URLRouter.generateUrl(UrlMethod.Resource, typeof(Workflow), new string[] { workflowID, typeof(Item).FullName.Split('.').Last().ToLower() + "s" });
            
            IList<Item> eleList;
            try
            {
                eleList = GetElementList<Item>(url);
            } catch(BasicException)
            {
                throw;
            } 

            return eleList;
        }

        /// <summary>
        ///  General method to get a list of objects (RootElements: Workflow, Item or User) from server.
        /// </summary>
        /// <typeparam name="O">Type of handled object</typeparam>
        /// <param name="url">Request URL</param>
        /// <returns>List of handled objects</returns>
        private IList<O> GetElementList<O>(string url) 
        {
            IRestResponse response = null;
            var request = createRequest(url, Method.GET);
            try
            {
                response = InternalRequester.RetrieveRequest(request);
            }
            catch (BasicException e)
            {
                System.Diagnostics.Trace.WriteLine("Exception " + e.Data);

                throw;
            } 

            IList<O> eleList = JsonConvert.DeserializeObject<List<O>>(response.Content, _jsonSettings);
            return eleList;
        }

        /// <summary>
        ///     Get an object from the server, with HTTP-Method GET.
        ///     Path for this HTTP-Method is always: ressource/<typename>/<id>/
        /// </summary>
        /// <typeparam name="O">Type of the requested object</typeparam>
        /// <param name="id">Id of the requested object</param>
        /// <returns>The requested object</returns>
        public O GetObject<O>(string id) where O : new()
        {
            IRestResponse response;
            String url = URLRouter.generateUrl(UrlMethod.Resource, typeof(O), new string[] { id });
            
            var request = createRequest(url, Method.GET);
            
            try
            {
                // call Internal Requester to finally send the request
                response = InternalRequester.RetrieveRequest(request);
            }
            catch (BasicException)
            {
                throw;
            }

            //Deserialize JSON
            O desObj = JsonConvert.DeserializeObject<O>(response.Content, _jsonSettings);

            if (desObj.GetType() == typeof(Workflow))
            {
                convertIdListToReferences(ChangeType<Workflow>(desObj));
            }

            return desObj;
        }

        /// <summary>
        ///     Update an object on the server, with HTTP-Method PUT. Path if sendObj is Workflow or Item: 'resource/<typename>/<id>', if user:  'resource/<typename>/<username>'
        /// </summary>
        /// <param name="sendObj">The object to update</param>
        /// <returns>If it worked or not</returns>
        public Boolean UpdateObject(RootElement sendObj)
        {
            IRestResponse response;
            String url = URLRouter.generateUrl(UrlMethod.Resource, sendObj.GetType(), new string[] { sendObj.id });
            
            Console.WriteLine("url: " + url);
            // Serialize to JSON
            String serializedObj = JsonConvert.SerializeObject(sendObj, _jsonSettings);

            var request = createRequest(url, Method.PUT);
            
            try
            {
                // call Internal Requester to finally send the request
                response = InternalRequester.SendRequest(request, serializedObj);
            }
            catch (BasicException)
            {
                throw;
            }

            return response.StatusCode == HttpStatusCode.OK;
        }

        /// <summary>
        ///     Create an object on the server, with HTTP-Method POST. Path is: 'resource/<typename>'
        /// </summary>
        /// <typeparam name="O">The type of the object to be created</typeparam>
        /// <param name="sendObj">The specified object to create</param>
        /// <returns>True if it worked, false/exception otherwise</returns>
        public Boolean PostObject<O>(O sendObj)
        {
            IRestResponse response;
            String url = URLRouter.generateUrl(UrlMethod.Resource, typeof(O));
            
            // Serialize to JSON
            String serializedObj = JsonConvert.SerializeObject(sendObj, _jsonSettings);
            // Create request
            var request = createRequest(url, Method.POST);
            
            try
            {
                // call Internal Requester to finally send the request
                response = InternalRequester.SendRequest(request, serializedObj);
            }
            catch (BasicException)
            {
                throw;
            }
            return response.StatusCode == HttpStatusCode.OK;
        }

        /// <summary>
        ///  Delete an object (Item or Workflow) on the server, with HTTP-Method DEL. Path is: 'resource/<typename>/<resId>'
        /// </summary>
        /// <typeparam name="O">Type of the delete object</typeparam>
        /// <param name="id">ID of the object to delete</param>
        /// <returns>The deleted Object</returns>
        public O DeleteObject<O>(string id) where O : new()
        {
            String url = URLRouter.generateUrl(UrlMethod.Resource, typeof(O), new string[] { id });
            var request = createRequest(url, Method.DELETE);
            return Delete<O>(request);
        }

        /// <summary>
        ///  General method to delete an object (RootElement: Workflow, Item, User).
        /// </summary>
        /// <typeparam name="O">Type of the object to delete</typeparam>
        /// <param name="url">Requested URL</param>
        /// <returns>Deleted object</returns>
        private O Delete<O>(RestRequest request) where O : new()
        {
            IRestResponse response;
            try
            {
                // call Internal Requester to finally send the request
                response = InternalRequester.RetrieveRequest(request);
            }
            catch (BasicException)
            {
                throw;
            }

            //Deserialize JSON
            O desObj = JsonConvert.DeserializeObject<O>(response.Content, _jsonSettings);

            return desObj;
        }

        /// <summary>
        ///     Does a login access to the server. Path ist always: '/command/user/login'
        /// </summary>
        /// <param name="username">Name of the user</param>
        /// <param name="password">Password of the user</param>
        /// <returns>True if it worked, false otherwhise, or an exception</returns>
        public Boolean checkUser(String username, SecureString password)
        {
            IRestResponse response;
            String url = URLRouter.generateUrl(UrlMethod.Operation, typeof(User), new string[] { "login" });
            
            // Create the RestRequest to send to server.
            var request = createRequest(url, Method.POST);
           
            try
            {
                // call Internal Requester to finally send the request
                response = InternalRequester.RetrieveRequest(request);
            }
            catch (BasicException)
            {
                throw;
            }

            return response.StatusCode == HttpStatusCode.OK;
        }

        /// <summary>
        ///     Sends a request to the server to start a workflow (create an item). Path is always: '/command/workflow/start/{workflowId}'
        /// </summary>
        /// <param name="wId">Workflow-Id</param>
        /// <param name="uId">Username</param>
        public Boolean StartWorkflow(string wId)
        {
            IRestResponse response;
            String url = URLRouter.generateUrl(UrlMethod.Operation, typeof(Workflow), new string[] { "start", wId });
            
            // Create the RestRequest to send to server.
            var request = createRequest(url, Method.POST);
            
            try
            {
                // call Internal Requester to finally send the request
                response = InternalRequester.RetrieveRequest(request);
            }
            catch (BasicException)
            {
                throw;
            }

            return response.StatusCode == HttpStatusCode.OK;
        }

        /// <summary>
        ///     Sends a state change of an action to the server. Path is always:  '/command/workflow/forward/{stepId}/{itemId}'
        /// </summary>
        /// <param name="stepId">Id of the current step</param>
        /// <param name="itemId">Id of the current item</param>
        /// <param name="uId">Name of the current user</param>
        /// <returns>True if it worked, false/exception otherwise</returns>
        public Boolean StepForward(string stepId, string itemId)
        {
            IRestResponse response;
            String url = URLRouter.generateUrl(UrlMethod.Operation, typeof(Workflow), new string[] { "forward", stepId, itemId });
            
            // Create the RestRequest to send to server.
            var request = createRequest(url, Method.POST);
            
            try
            {
                // call Internal Requester to finally send the request
                response = InternalRequester.RetrieveRequest(request);
            }
            catch (BasicException)
            {
                throw;
            }
            return response.StatusCode == HttpStatusCode.OK;
        }

        /// <summary>
        ///  Create the request with needed header fields.
        /// </summary>
        /// <param name="url">the requested url</param>
        /// <param name="method">the http method</param>
        /// <returns>the generated request</returns>
        private RestRequest createRequest(string url, Method method)
        {
            var request = new RestRequest(url, method);
            request.AddHeader("Accept", "text/plain");

            request.AddParameter("username", _myUsername, ParameterType.HttpHeader);
            if(_myPassword != null)
                request.AddParameter("password", _myPassword, ParameterType.HttpHeader);

            return request;
        }
        

        /// <summary>
        /// This method changes the type of a generic object.
        /// </summary>
        /// <typeparam name="T">The expected new type.</typeparam>
        /// <param name="obj">The object</param>
        /// <returns>The object with changed type</returns>
        public static T ChangeType<T>(object obj)
        {
            return (T)Convert.ChangeType(obj, typeof(T));
        }

        /// <summary>
        /// Incoming order of step ids are converted into references.
        /// </summary>
        /// <param name="workflow">The worklow which steps are handled</param>
        public static void convertIdListToReferences(Workflow workflow)
        {
            foreach (Step s in workflow.steps)
            {
                foreach (String id in s.nextStepIds)
                {
                    s.nextSteps.Add(workflow.getStepById(id));
                }
            }

        }


        public bool SwitchElementActivity<O>(string id, string state)
        {
            throw new NotImplementedException();
        }
    }
}