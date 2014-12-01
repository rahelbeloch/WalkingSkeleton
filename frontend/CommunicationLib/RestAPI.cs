using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using RestSharp;
using System.Web;
using CommunicationLib.Model;
using System.Collections;
using System.Reflection;
using Newtonsoft.Json;
using System.Configuration;
using System.Net;
using System.Security;

namespace RestAPI
{
    /// <summary>
    ///     Static class, that realizes the Connection to the server.
    /// </summary>
    public class RestRequester : CommunicationLib.IRestRequester
    {
        public static String restserverurl;
        public static RestClient client;
        private static String _ressourceParam, _operationParam;
        private static JsonSerializerSettings _jsonSettings;

        ///<summary>
        ///     Static Constructor - is called automatically at first use of the class.
        /// </summary>
        static RestRequester()
        {
            restserverurl = Constants.serverUrl;
            client = new RestClient(restserverurl);
            _ressourceParam = "resource/";
            _operationParam = "command/";
            _jsonSettings = new JsonSerializerSettings { TypeNameHandling = TypeNameHandling.None, Formatting = Formatting.Indented};
        }

        
        /// <summary>
        ///     Requests all Objects (Items, Workflows or Users) belonging to the given user.
        /// </summary>
        /// <typeparam name="RootElementList">The list of RootElements</typeparam>
        /// <param name="userName">The users name</param>
        /// <returns>The list with RootElements requested from server</returns>
        public static IList<RootElement> GetAllObjects<RootElement>(String userName) where RootElement : new()
        {
            String typeName = typeof(RootElement).FullName.Split('.').Last().ToLower();
            // if userName is not null, it is concatenated to the url, otherwise path  is just 'resource/workflows' and will request all all workflows
            String url = _ressourceParam + typeName + "s" + (userName != null? "/" + userName : "");
            System.Diagnostics.Trace.WriteLine("url: " + url);
            var request = new RestRequest(url, Method.GET);
            request.AddHeader("Accept", "text/plain");

            // decide wether the server does return the right excepted object or throws an exception
            try
            {      
                var response = client.Execute(request);
                IList<RootElement> eleList = JsonConvert.DeserializeObject<List<RootElement>>(response.Content, _jsonSettings);
                return eleList;
            }
            catch (Exception)
            {
                var response = client.Execute<Exception>(request);
                throw response.Data;
            }
        }

        /// <summary>
        ///     Get an object from the server, with HTTP-Method GET.
        ///     Path for this HTTP-Method is always: ressource/<typename>/<id>/
        /// </summary>
        /// <typeparam name="O">Type of the requested object</typeparam>
        /// <param name="id">Id of the requested object</param>
        /// <returns>The requested object</returns>
        public static O GetObject<O>(int id) where O : new()
        {
            String typeName = typeof(O).FullName.Split('.').Last().ToLower();
            String url = _ressourceParam + typeName +"/" + id;
           IRestResponse response = GetObjectRequest<O>(url, Method.GET);

            //Deserialize JSON
            O desObj = JsonConvert.DeserializeObject<O>(response.Content, _jsonSettings);

            return desObj;
        }

        /// <summary>
        ///     Update an object on the server, with HTTP-Method PUT. Path is always: 'resource/<typename>/<objId>'
        /// </summary>
        /// <param name="sendObj">The object to update</param>
        /// <returns>If it worked or not</returns>
        public static Boolean UpdateObject(RootElement sendObj)
        {
            String typeName = sendObj.GetType().FullName.Split('.').Last().ToLower();
            String url = _ressourceParam + typeName + "/" + sendObj.id;

            // Serialize to JSON
            String serializedObj = JsonConvert.SerializeObject(sendObj, _jsonSettings);
            
            IRestResponse resp = SendObjectRequest(url, Method.PUT, serializedObj);

            return  resp.StatusCode == HttpStatusCode.OK;
        }

        /// <summary>
        ///     Create an object on the server, with HTTP-Method POST. Path is: 'resource/<typename>'
        /// </summary>
        /// <typeparam name="O">The type of the object to be created</typeparam>
        /// <param name="sendObj">The specified object to create</param>
        /// <returns>True if it worked, false/exception otherwise</returns>
        public static Boolean PostObject<O>(RootElement sendObj) where O : new()
        {
            String typeName = typeof(O).FullName.Split('.').Last().ToLower();
            String url = _ressourceParam + typeName;
            
            // Serialize to JSON
            String serializedObj = JsonConvert.SerializeObject(sendObj, _jsonSettings);
            
            IRestResponse resp = SendObjectRequest(url, Method.POST, serializedObj);

            return resp.StatusCode == HttpStatusCode.OK;
        }

        /// <summary>
        ///     Delete an object on the server, with HTTP-Method DEL. Path is: 'resource/<typename>/<resId>'
        /// </summary>
        /// <typeparam name="O">Type of the deleted object</typeparam>
        /// <param name="id">Id of the deleted object</param>
        /// <returns>The deleted object</returns>
        public static O DeleteObject<O>(int id) where O : new()
        {
            String typeName = typeof(O).FullName.Split('.').Last().ToLower();
            String url = _ressourceParam + typeName + "/" + id;
            IRestResponse response = GetObjectRequest<O>(url, Method.DELETE);

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
        public static Boolean checkUser(String username, SecureString password)
        {
            String url = _operationParam + "user/" + "login";

            var request = new RestRequest(url, Method.POST);
            request.AddHeader("Accept", "text/plain");
            request.AddParameter("username", username, ParameterType.GetOrPost);
            request.AddParameter("password", password, ParameterType.GetOrPost);

            IRestResponse resp = SendSimpleRequest(request);

            return resp.StatusCode == HttpStatusCode.OK;
        }


        /// <summary>
        ///     Sends a request to the server to start a workflow (create an item). Path is always: '/command/workflow/start/{workflowId}/{userName}'
        /// </summary>
        /// <param name="wId">Workflow-Id</param>
        /// <param name="uId">Username</param>
        public static Boolean StartWorkflow(int wId, string username)
        {
            String url = _operationParam + "workflow/"+ "start/" + wId + "/" + username;

            var request = new RestRequest(url, Method.POST);
            request.AddHeader("Accept", "text/plain");

            IRestResponse resp = SendSimpleRequest(request);
            
            return resp.StatusCode == HttpStatusCode.OK ;
        }

        /// <summary>
        ///     Sends a state change of an action to the server. Path is always:  '/command/workflow/forward/{stepId}/{itemId}/{username}'
        /// </summary>
        /// <param name="stepId">Id of the current step</param>
        /// <param name="itemId">Id of the current item</param>
        /// <param name="uId">Name of the current user</param>
        /// <returns>True if it worked, false/exception otherwise</returns>
        public static Boolean StepForward(int stepId, int itemId, string username)
        {
            String url = _operationParam + "workflow/" + "forward/" + stepId + "/" + itemId + "/" + username;
            
            var request = new RestRequest(url, Method.POST);
            request.AddHeader("Accept", "text/plain");
            // Parameters are set in URL; alternative is adding them to the request
            //request.AddParameter("username", username, ParameterType.GetOrPost);
            //request.AddParameter("password", password, ParameterType.GetOrPost);

            IRestResponse resp = SendSimpleRequest(request);
            
            return resp.StatusCode == HttpStatusCode.OK;
        }

        /// <summary>
        ///     Sends a HTTP-Request to the server to get an object.
        /// </summary>
        /// <typeparam name="O">Type of the requested object</typeparam>
        /// <param name="url">Requested url</param>
        /// <param name="method">HTTP-Method of the request</param>
        /// <returns>Response object from server</returns>
        private static IRestResponse GetObjectRequest<O>(String url, RestSharp.Method method) where O : new()
        {
            var request = new RestRequest(url, method);
            request.AddHeader("Accept", "text/plain");

            // decide wether the server does return the right excepted object or throws an exception
            try
            {
                var response = client.Execute(request);
                return response;
            }
            catch (Exception)
            {
                var response = client.Execute<Exception>(request);
                throw response.Data;
            }
        }

        /// <summary>
        ///     Method to send a C# Object to server.
        /// </summary>
        /// <param name="url">Request url</param>
        /// <param name="method">Method of the request</param>
        /// <param name="serializedObjPath">JSON string serialized object</param>
        /// <returns>The response from server</returns>
        private static IRestResponse SendObjectRequest(String url, RestSharp.Method method, String serializedObj)
        {
            var request = new RestRequest(url, method);
            request.AddHeader("Accept", "text/plain");

            // if there is an object that shall be send to server
            if (serializedObj != null)
            {
                request.RequestFormat = RestSharp.DataFormat.Json;
                request.AddParameter("data", serializedObj, ParameterType.GetOrPost);
            }

            // decide wether the server does return the right excepted object or throws an exception
            try
            {
                var response = client.Execute(request);
                return response;
            }
            catch (Exception)
            {
                var response = client.Execute<Exception>(request);
                throw response.Data;
            }
        }

        /// <summary>
        ///     Sends a simple request, just containing some information in the url. No more parameters or objects send in the request.
        /// </summary>
        /// <param name="request">The request to send to server</param>
        /// <returns>The response object</returns>
        private static IRestResponse SendSimpleRequest(RestRequest request)
        {
            try
            {
                var response = client.Execute(request);
                return response;
            }
            catch(Exception)
            {
                var response = client.Execute<Exception>(request);
                throw response.Data;
            }
        }
    }
}