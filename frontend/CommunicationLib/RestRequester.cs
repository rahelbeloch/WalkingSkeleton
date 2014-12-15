﻿using System;
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

        public IList<O> GetAllElements<O>()
        {
            String url = URLRouter.generateUrl(UrlMethod.Resource, typeof(O));
            //String typeName = typeof(O).FullName.Split('.').Last().ToLower();
            //String url = _ressourceParam + typeName + "s";

            IList<O> eleList;
            try
            {
                eleList = GetElementList<O>(url, "");
            }
            catch (BasicException)
            {
                throw;
            }
            return eleList;
        }

        /*
        /// <summary>
        ///  Method to retrieve all existent workflows on server.
        /// </summary>
        /// <returns>List of all workflows</returns>
        private IList<Workflow> GetAllWorkflows()
        {
            String typeName = typeof(Workflow).FullName.Split('.').Last().ToLower();
            String url = _ressourceParam + typeName + "s";

            IList<Workflow> eleList;
            try
            {
                eleList = GetElementList<Workflow>(url,"");
            }
            catch (BasicException)
            {
                throw;
            }
            return eleList;
        }
         **/

        /// <summary>
        ///  Method to retrieve all existent workflows of one given user on server.
        /// </summary>
        /// <param name="username">Requested username</param>
        /// <returns>List of all workflow of this user</returns>
        public IList<Workflow> GetAllWorkflowsByUser(String userName)
        {
            String url = URLRouter.generateUrl(UrlMethod.Resource, typeof(Workflow));
            //String typeName = typeof(Workflow).FullName.Split('.').Last().ToLower();
            //String url = _ressourceParam + typeName + "s";

            IList<Workflow> eleList;
            try
            {
                eleList = GetElementList<Workflow>(url, userName);
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
        public IList<int> GetStartablesByUser(string userName)
        {
            IRestResponse response;

            String url = URLRouter.generateUrl(UrlMethod.Resource, typeof(Workflow), new string[] { "startables" });
            //String typeName = typeof(Workflow).FullName.Split('.').Last().ToLower();
            //String url = _ressourceParam + typeName + "s/startables/";

            var request = new RestRequest(url, Method.GET);
            request.AddParameter("username", userName, ParameterType.HttpHeader);
            request.AddHeader("Accept", "text/plain");

            try
            {
                // call Internal Requester to finally send the request
                response = InternalRequester.RetrieveRequest(request);
                Console.WriteLine(response.Content);
            }
            catch (BasicException)
            {
                throw;
            }

            // Deserialization
            IList<int> eleList = JsonConvert.DeserializeObject<List<int>>(response.Content, _jsonSettings);
            
            return eleList;
        }

        /// <summary>
        ///  Method to retrieve all relevant items of one given user. Relevant means all items where the user can accept or close actions.
        /// </summary>
        /// <param name="workflowID">The actual handled workflow</param>
        /// <param name="username">The requested user</param>
        /// <returns>List of relevant items</returns>
        public IList<Item> GetRelevantItemsByUser(int workflowID, string userName)
        {
            String url = URLRouter.generateUrl(UrlMethod.Resource, typeof(Item), new string[] { workflowID.ToString() });
            //String typeName = typeof(Item).FullName.Split('.').Last().ToLower();
            //String url = _ressourceParam + typeName + "s" + "/" + workflowID;

            IList<Item> eleList;
            try
            {
                eleList = GetElementList<Item>(url, userName);
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
        private IList<O> GetElementList<O>(string url, string username) 
        {
            IRestResponse response;

            var request = new RestRequest(url, Method.GET);
            request.AddParameter("username", username, ParameterType.HttpHeader);
            request.AddHeader("Accept", "text/plain");

            try
            {
                response = InternalRequester.RetrieveRequest(request);
            }
            catch (BasicException)
            {
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
        public O GetObject<O>(int id) where O : new()
        {
            IRestResponse response;

            String url = URLRouter.generateUrl(UrlMethod.Resource, typeof(O), new string[] { id.ToString() });
            //String typeName = typeof(O).FullName.Split('.').Last().ToLower();
            //String url = _ressourceParam + typeName + "s/" + id;

            var request = new RestRequest(url, Method.GET);
            request.AddHeader("Accept", "text/plain");

            try
            {
                // call Internal Requester to finally send the request
                //response = InternalRequester.RetrieveRequest(url, Method.GET);
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

            String url = URLRouter.generateUrl(UrlMethod.Resource, typeof(RootElement), new string[] { sendObj.id.ToString() });
            //String typeName = sendObj.GetType().FullName.Split('.').Last().ToLower();
            //String url = _ressourceParam + typeName + "s/";
            //url += sendObj.GetType() == typeof(User) ? "" : sendObj.id.ToString();

            // Serialize to JSON
            String serializedObj = JsonConvert.SerializeObject(sendObj, _jsonSettings);

            var request = new RestRequest(url, Method.PUT);
            if(sendObj.GetType() == typeof(User))
                request.AddParameter("username", ((User)sendObj).username, ParameterType.HttpHeader);
            request.AddHeader("Accept", "text/plain");

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

        public Boolean SwitchElementActivity<O>(string id, string state)
        {
            IRestResponse response;
            String url = URLRouter.generateUrl(UrlMethod.Resource, typeof(O), new string[] { id, state });

            var request = new RestRequest(url, Method.PUT);
            request.AddHeader("Accept", "text/plain");

            try
            {
                response = InternalRequester.RetrieveRequest(request);
            }
            catch (BasicException)
            {
                throw;
            }
            return response.StatusCode == HttpStatusCode.OK;
        }

        /// <summary>
        ///     Send workflow activity change to server. Path is always: '/ressourceParam/workflow/{workflowid}/{state}
        /// </summary>
        /// <param name="workflowId">Id of workflow which should be (de-)activated</param> 
        /// <param name="state">state which indicates if workflow should be (de-)activated</param>
        /// <returns></returns>
        public Boolean WorkflowActivity(int workflowId, string state)
        {
            IRestResponse response;
            String url = URLRouter.generateUrl(UrlMethod.Resource, typeof(Workflow), new string[] { workflowId.ToString(), state });
            //String url = _ressourceParam + "workflow/" + workflowId + "/" + state;

            var request = new RestRequest(url, Method.PUT);
            request.AddHeader("Accept", "text/plain");

            try
            {
                response = InternalRequester.RetrieveRequest(request);
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
            //String typeName = typeof(O).FullName.Split('.').Last().ToLower();
            //String url = _ressourceParam + typeName + "s";

            // Serialize to JSON
            String serializedObj = JsonConvert.SerializeObject(sendObj, _jsonSettings);

            var request = new RestRequest(url, Method.POST);
            request.AddHeader("Accept", "text/plain");

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
        public O DeleteObject<O>(int id) where O : new()
        {
            String url = URLRouter.generateUrl(UrlMethod.Resource, typeof(O), new string[] { id.ToString() });
            //String typeName = typeof(O).FullName.Split('.').Last().ToLower();
            //String url = _ressourceParam + typeName + "s/" + id;

            var request = new RestRequest(url, Method.DELETE);
            request.AddHeader("Accept", "text/plain");

            return Delete<O>(request);
        }

        /// <summary>
        ///  Method to delete a user.
        /// </summary>
        /// <param name="username">The bad bad user to delete</param>
        /// <returns>The deleted user</returns>
        public User DeleteUser(string username)
        {
            String url = URLRouter.generateUrl(UrlMethod.Resource, typeof(User));
            //String typeName = typeof(User).FullName.Split('.').Last().ToLower();
            //String url = _ressourceParam + typeName + "s";

            var request = new RestRequest(url, Method.DELETE);
            request.AddParameter("username", username, ParameterType.HttpHeader);
            request.AddHeader("Accept", "text/plain");

            return Delete<User>(request);
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
            //String url = _operationParam + "users/" + "login";
            
            // Create the RestRequest to send to server.
            var request = new RestRequest(url, Method.POST);
            request.AddHeader("Accept", "text/plain");
            request.AddParameter("username", username, ParameterType.HttpHeader);
            request.AddParameter("password", password, ParameterType.HttpHeader);

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
        ///     Sends a request to the server to start a workflow (create an item). Path is always: '/command/workflow/start/{workflowId}/{userName}'
        /// </summary>
        /// <param name="wId">Workflow-Id</param>
        /// <param name="uId">Username</param>
        public Boolean StartWorkflow(int wId, string username)
        {
            IRestResponse response;
            String url = URLRouter.generateUrl(UrlMethod.Operation, typeof(Workflow), new string[] { "start", wId.ToString() });
            //String url = _operationParam + "workflows/" + "start/" + wId.ToString();

            // Create the RestRequest to send to server.
            var request = new RestRequest(url, Method.POST);
            request.AddParameter("username", username, ParameterType.HttpHeader);
            request.AddHeader("Accept", "text/plain");

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
        ///     Sends a state change of an action to the server. Path is always:  '/command/workflow/forward/{stepId}/{itemId}/{username}'
        /// </summary>
        /// <param name="stepId">Id of the current step</param>
        /// <param name="itemId">Id of the current item</param>
        /// <param name="uId">Name of the current user</param>
        /// <returns>True if it worked, false/exception otherwise</returns>
        public Boolean StepForward(int stepId, int itemId, string username)
        {
            IRestResponse response;
            String url = URLRouter.generateUrl(UrlMethod.Operation, typeof(Workflow), new string[] { "forward", stepId.ToString(), itemId.ToString() });
            //String url = _operationParam + "workflows/" + "forward/" + stepId + "/" + itemId;

            // Create the RestRequest to send to server.
            var request = new RestRequest(url, Method.POST);
            request.AddParameter("username", username, ParameterType.HttpHeader);
            request.AddHeader("Accept", "text/plain");

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

        /*
        private void addAuthentification(RestRequest request, string username, string password)
        {
            request.AddParameter("username", username, ParameterType.HttpHeader);
            if(password != null)
                request.AddParameter("password", password, ParameterType.HttpHeader);
        }
         * */

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
                foreach (int id in s.nextStepIds)
                {
                    s.nextSteps.Add(workflow.getStepById(id));
                }
            }

        }
    }
}