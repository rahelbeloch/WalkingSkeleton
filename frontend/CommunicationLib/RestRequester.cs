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
    public class RestRequester : CommunicationLib.IRestRequester
    {

        private static String _ressourceParam, _operationParam;
        private static JsonSerializerSettings _jsonSettings;

        public RestRequester()
        {
            _ressourceParam = "resource/";
            _operationParam = "command/";
            _jsonSettings = new JsonSerializerSettings
            {
                TypeNameHandling = TypeNameHandling.Auto,
                Formatting = Formatting.Indented,
                Binder = new CustomSerializationBinder()
            };
        }

        /// <summary>
        /// 
        /// </summary>
        /// <returns></returns>
        public IList<Workflow> GetAllWorkflows()
        {
            String typeName = typeof(Workflow).FullName.Split('.').Last().ToLower();
            String url = _ressourceParam + typeName + "s";

            return GetElementList<Workflow>(url);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="username"></param>
        /// <returns></returns>
        public IList<Workflow> GetAllWorkflowsByUser(String userName)
        {
            String typeName = typeof(Workflow).FullName.Split('.').Last().ToLower();
            String url = _ressourceParam + typeName + "s" + "/" + userName;

            return GetElementList<Workflow>(url);
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="username"></param>
        /// <returns></returns>
        public IList<int> GetStartablesByUser(string userName)
        {
            IRestResponse resp;
            String url = _ressourceParam + "startables/" + userName;

            var request = new RestRequest(url, Method.POST);
            request.AddHeader("Accept", "text/plain");

            try
            {
                resp = InternalRequester.SendSimpleRequest(request);
            }
            catch (BasicException)
            {
                throw;
            }

            IList<int> eleList = JsonConvert.DeserializeObject<List<int>>(resp.Content, _jsonSettings);

            return eleList;
        }

        /// <summary>
        /// 
        /// </summary>
        /// <param name="workflowID"></param>
        /// <param name="username"></param>
        /// <returns></returns>
        public IList<Item> GetRelevantItemsByUser(int workflowID, string userName)
        {
            String typeName = typeof(Item).FullName.Split('.').Last().ToLower();
            String url = _ressourceParam + typeName + "s" + "/" + userName + "/" + workflowID;

            return GetElementList<Item>(url);
        }

        private IList<O> GetElementList<O>(string url) 
        {
            IRestResponse response = InternalRequester.GetAllObjects<O>(url, Method.GET);
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
            String typeName = typeof(O).FullName.Split('.').Last().ToLower();
            String url = _ressourceParam + typeName + "/" + id;
            try
            {
                response = InternalRequester.GetObjectRequest<O>(url, Method.GET);
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
        ///     Update an object on the server, with HTTP-Method PUT. Path is always: 'resource/<typename>/<objId>'
        /// </summary>
        /// <param name="sendObj">The object to update</param>
        /// <returns>If it worked or not</returns>
        public Boolean UpdateObject(RootElement sendObj)
        {
            IRestResponse response;
            String typeName = sendObj.GetType().FullName.Split('.').Last().ToLower();

            String url = _ressourceParam + typeName + "/";
            url += sendObj.GetType() == typeof(User) ? ((User)sendObj).username : sendObj.id.ToString();

            // Serialize to JSON
            String serializedObj = JsonConvert.SerializeObject(sendObj, _jsonSettings);
            try
            {
                response = InternalRequester.SendObjectRequest(url, Method.PUT, serializedObj);
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
            String typeName = typeof(O).FullName.Split('.').Last().ToLower();
            String url = _ressourceParam + typeName;

            // Serialize to JSON
            String serializedObj = JsonConvert.SerializeObject(sendObj, _jsonSettings);

            try
            {
                response = InternalRequester.SendObjectRequest(url, Method.POST, serializedObj);
            }
            catch (BasicException)
            {
                throw;
            }

            return response.StatusCode == HttpStatusCode.OK;
        }

        /// <summary>
        ///     Delete an object on the server, with HTTP-Method DEL. Path is: 'resource/<typename>/<resId>'
        /// </summary>
        /// <typeparam name="O">Type of the deleted object</typeparam>
        /// <param name="id">Id of the deleted object</param>
        /// <returns>The deleted object</returns>
        public O DeleteObject<O>(int id) where O : new()
        {
            IRestResponse response;
            String typeName = typeof(O).FullName.Split('.').Last().ToLower();
            String url = _ressourceParam + typeName + "/" + id;
            try
            {
                response = InternalRequester.GetObjectRequest<O>(url, Method.DELETE);
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
            IRestResponse resp;
            String url = _operationParam + "user/" + "login";

            var request = new RestRequest(url, Method.POST);
            request.AddHeader("Accept", "text/plain");
            request.AddParameter("username", username, ParameterType.GetOrPost);
            request.AddParameter("password", password, ParameterType.GetOrPost);

            try
            {
                resp = InternalRequester.SendSimpleRequest(request);
            }
            catch (BasicException)
            {
                throw;
            }

            return resp.StatusCode == HttpStatusCode.OK;
        }

        /// <summary>
        ///     Sends a request to the server to start a workflow (create an item). Path is always: '/command/workflow/start/{workflowId}/{userName}'
        /// </summary>
        /// <param name="wId">Workflow-Id</param>
        /// <param name="uId">Username</param>
        public Boolean StartWorkflow(int wId, string username)
        {
            IRestResponse resp;
            String url = _operationParam + "workflow/" + "start/" + wId.ToString() + "/" + username;

            var request = new RestRequest(url, Method.POST);
            request.AddHeader("Accept", "text/plain");

            try
            {
                resp = InternalRequester.SendSimpleRequest(request);
            }
            catch (BasicException)
            {
                throw;
            }

            return resp.StatusCode == HttpStatusCode.OK;
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
            IRestResponse resp;
            String url = _operationParam + "workflow/" + "forward/" + stepId + "/" + itemId + "/" + username;

            var request = new RestRequest(url, Method.POST);
            request.AddHeader("Accept", "text/plain");
            // Parameters are set in URL; alternative is adding them to the request
            //request.AddParameter("username", username, ParameterType.GetOrPost);
            //request.AddParameter("password", password, ParameterType.GetOrPost);

            try
            {
                resp = InternalRequester.SendSimpleRequest(request);
            }
            catch (BasicException)
            {
                throw;
            }
            return resp.StatusCode == HttpStatusCode.OK;
        }

        /// <summary>
        /// This method changes the type of a generic object.
        /// </summary>
        /// <typeparam name="T">The expected new type.</typeparam>
        /// <param name="obj"></param>
        /// <returns></returns>
        public static T ChangeType<T>(object obj)
        {
            return (T)Convert.ChangeType(obj, typeof(T));
        }

        /// <summary>
        /// Incoming order of step ids are converted into references.
        /// </summary>
        /// <param name="workflow"></param>
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