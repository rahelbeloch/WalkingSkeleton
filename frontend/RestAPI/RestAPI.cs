using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using RestSharp;
using System.Web;
using System.Xml;
using System.Xml.Serialization;
using System.IO;
using CommunicationLib.Model;
using System.Collections;
using System.Reflection;

namespace RestAPI
{
    public class OwnException : Exception
    {
        public OwnException()
        {
        }

        public OwnException(String msg):base(msg)
        {
        }
    }

    /// <summary>
    ///     Program class to test the connection.
    /// </summary>
    public class Programm
    {


        /// <summary>
        ///     Method to test the Connection.
        /// </summary>
        /// <param name="args"></param>
        public static void Main(string[] args)
        {

            RestRequester.init();

            // Test to send a Workflow - DONE
            AbstractWorkflow testWF = new AbstractWorkflow();
            testWF.Id = 17;
            /*AbstractStartStep ass = new AbstractStartStep();
            ass.Name = "Step1";
            ass.Username = "Rahel";
            ass.UserId = 17;
            ass.Id = 0;
            ass.label = "Label";
            testWF.addStep(ass);
            testWF.addStep(new AbstractAction());
            //testWF.addStep(new AbstractFinalStep());*/
            //String answer = RestRequester.PostObject<AbstractWorkflow>(testWF);

            var requeststadtpost = new RestRequest("resource/abstractworkflow/17", Method.POST);
            requeststadtpost.AddHeader("Accept", "text/plain");
            requeststadtpost.AddParameter("data",testWF.ToString(),ParameterType.RequestBody);
            
            var respstadtpost = RestRequester.client.Execute(requeststadtpost);

            Console.WriteLine("Antwort: " + respstadtpost.StatusDescription + "/" + respstadtpost.Content);

            // Test to get a workflow - DONE
            AbstractWorkflow getWF = RestRequester.GetObject<AbstractWorkflow>(17);
            Console.WriteLine("GET-Workflow Anfrage erfolgreich geschickt...");
            Console.WriteLine("Object bekommen: " + getWF);
            //Console.WriteLine("Antwort: Workflow Nr. " + getWF.Id + " bekommen.");

            // Test to start a workflow - DONE
            Boolean done = RestRequester.StartWorkflow(1, "Rahel");
            Boolean d = RestRequester.StartWorkflow(5, "Kalle");
            Boolean don = RestRequester.StartWorkflow(10, "Tilman");
            Console.WriteLine("Start-Workflow Anfrage erfolgreich geschickt...");
            Console.WriteLine("Antwort: Workflow gestartet: " + done);

            // Step Forward
            RestRequester.StepForward(5,11,"Rahel");

            // updateObject
            RestRequester.UpdateObject(testWF);

            // deleteObject
            RestRequester.DeleteObject<AbstractWorkflow>(1);

            Console.ReadKey();
        }
    }

    /// <summary>
    ///     Static class, that realizes the Connection to the server.
    /// </summary>
    public class RestRequester
    {
        public static String restserverurl;
        public static RestClient client;
        private static String _ressourceParam = "resource/";
        private static String _operationParam = "command/";

        ///<summary>
        ///     Initializes the RestClient. Has to be called before first use.
        /// </summary>
        public static void init()
        {
            restserverurl = "http://172.26.38.108:8080";
            client = new RestClient(restserverurl);
        }

        /// <summary>
        ///     Requests all Objects (Items or Workflows) belonging to the given user.
        /// </summary>
        /// <typeparam name="ElementList">the list of AbstractElements</typeparam>
        /// <param name="userName">name of the user</param>
        /// <returns>the list with AbstractElements requested from server</returns>
        public static ElementList GetAllObjects<ElementList>(String userName) where ElementList : new()
        {
            String typeName = typeof(AbstractElement).FullName.Split('.').Last().ToLower();
            String url = "getall/" + typeName + "/" + userName;
           
            var request = new RestRequest(url, Method.GET);

            // decide wether the server does return the right excepted object or throws an exception
            try
            {      
                var response = client.Execute<ElementList>(request);
                ElementList obj = response.Data;

                return obj;
            }
            catch (OwnException)
            {
                var response = client.Execute<Exception>(request);
                throw response.Data;
            }
        }

        /// <summary>
        ///     Get an object from the server, with HTTP-Method GET.
        ///     Path for this HTTP-Method is always: get/<type>/<id>
        /// </summary>
        /// <typeparam name="O">type of the requested object</typeparam>
        /// <param name="id">id of the requested object</param>
        /// <returns>the requested object</returns>
        public static O GetObject<O>(int id) where O : new()
        {
            String typeName = typeof(O).FullName.Split('.').Last().ToLower();
            String url = _ressourceParam + typeName +"/" + id;
            Console.WriteLine("Anfrageart: " + Method.GET);
            Console.WriteLine("AnfrageUrl: " + url);
            O request = GetObjectRequest<O>(url, Method.GET);

            return request;
        }

        /// <summary>
        ///     Update an object on the server, with HTTP-Method PUT.
        /// </summary>
        /// <param name="sendObj"></param>
        /// <returns></returns>
        public static String UpdateObject(AbstractElement sendObj)
        {
            String typeName = sendObj.GetType().FullName.Split('.').Last().ToLower();
            String url = _ressourceParam + typeName + "/" + sendObj.id;
            Console.WriteLine("Anfrageart: " + Method.PUT);
            Console.WriteLine("AnfrageUrl: " + url);
            String serializedObjPath = SerializeObject(sendObj);
            IRestResponse request = SendObjectRequest(url, Method.PUT, serializedObjPath);

            return request.Content;
        }

        /*
        * Create an object on the server, with HTTP-Method POST.
        * Path for this HTTP-Method is always: send/<type>
         * Answer: True/False als String
        */
        public static String PostObject<O>(AbstractElement sendObj) where O : new()
        {
            String typeName = typeof(O).FullName.Split('.').Last().ToLower();
            String url = _ressourceParam + typeName + "/" + sendObj.id;
            Console.WriteLine("Anfrageart: " + Method.POST);
            Console.WriteLine("AnfrageUrl: " + url);
            String serializedObjPath = SerializeObject(sendObj);
            IRestResponse request = SendObjectRequest(url, Method.POST, serializedObjPath);

            return request.Content;
        }

        /// <summary>
        ///     Delete an object on the server, with HTTP-Method DEL.
        /// </summary>
        /// <typeparam name="O">type of the deleted object</typeparam>
        /// <param name="id">id of the deleted object</param>
        /// <returns>the deleted object</returns>
        public static O DeleteObject<O>(int id) where O : new()
        {
            String typeName = typeof(O).FullName.Split('.').Last().ToLower();
            String url = _ressourceParam + typeName + "/" + id;
            Console.WriteLine("Anfrageart: " + Method.DELETE);
            Console.WriteLine("AnfrageUrl: " + url);
            O request = GetObjectRequest<O>(url, Method.DELETE);

            return request;
        }

        /// <summary>
        ///     Sends a request to the server to start a workflow (create an item)
        /// </summary>
        /// <param name="wId">Workflow-Id</param>
        /// <param name="uId">User-name</param>
        public static Boolean StartWorkflow(int wId, string username)
        {
            // 'command/start/workflowid/username'
            String url = _operationParam + "start/" + wId + "/" + username;
            var response = SendSimpleRequest(url, Method.POST);
            // do something with the response, e.g. look if everything is ok.
           
            return true;
        }

        /// <summary>
        ///     Sends a state change of an action to the server
        /// </summary>
        /// <param name="stepId">id of the current step</param>
        /// <param name="itemId">id of the current item</param>
        /// <param name="uId">name of the current user</param>
        public static Boolean StepForward(int stepId, int itemId, string username)
        {
            // Request url: 'command/forward/stepid/itemid/username'
            String url = _operationParam + "forward/" + stepId + "/" + itemId + "/" + username;
            IRestResponse response = SendSimpleRequest(url, Method.POST);
            // do something with the response, e.g. look if everything is ok.
            
            return true;
        }

        /// <summary>
        ///     Sends a HTTP-Request to the server to get an object.
        /// </summary>
        /// <typeparam name="O">the type of the requested object</typeparam>
        /// <param name="url">the requested url</param>
        /// <param name="method">the HTTP-Method of the request</param>
        /// <returns>the response object from server</returns>
        private static O GetObjectRequest<O>(String url, RestSharp.Method method) where O : new()
        {
            var request = new RestRequest(url, method);
            request.AddHeader("Accept", "text/plain");

            // decide wether the server does return the right excepted object or throws an exception
            try
            {
                var response = client.Execute<O>(request);
                O obj = response.Data;
                return obj;
            }
            catch (OwnException)
            {
                var response = client.Execute<Exception>(request);
                throw response.Data;
            }
        }

        /// <summary>
        ///     Method to send a C# Object to server. Expects an answer-string from server.
        /// </summary>
        /// <param name="url">request url</param>
        /// <param name="method">method of the request</param>
        /// <param name="serializedObjPath">path to the xml file with serialized object</param>
        /// <returns>the response from server</returns>
        private static IRestResponse SendObjectRequest(String url, RestSharp.Method method, String serializedObjPath)
        {
            var request = new RestRequest(url, method);
            Console.WriteLine(request);
            request.AddHeader("Accept", "text/plain");

            // if there is an object-path to a xml file that shall be send to server per XML
            if (serializedObjPath != null)
            {
                request.RequestFormat = RestSharp.DataFormat.Xml;
                request.AddParameter("data", File.ReadAllText(serializedObjPath), ParameterType.RequestBody);
            }

            // decide wether the server does return the right excepted object or throws an exception
            try
            {
                var response = client.Execute(request);
                Console.WriteLine("Post Request-Antwort: " + response);
                Console.WriteLine("Post Request-Antwort: " + response.ErrorMessage);
                Console.WriteLine("Post Request-Antwort: " + response.StatusCode + " / " + response.StatusDescription);
                return response;
            }
            catch (OwnException)
            {
                var response = client.Execute<Exception>(request);
                throw response.Data;
            }
        }

        /// <summary>
        ///     Sends a simple request, just containing some information in the url. No more parameters or objects send in the request.
        /// </summary>
        /// <param name="url">request url</param>
        /// <param name="method">method of the request</param>
        /// <returns>the response from server</returns>
        private static IRestResponse SendSimpleRequest(string url, RestSharp.Method method)
        {
            var request = new RestRequest(url, method);
            request.AddHeader("Accept", "text/plain");

            try
            {
                var response = client.Execute(request);
                return response;
            }
            catch(Exception e)
            {
                var response = client.Execute<Exception>(request);
                throw response.Data;
            }
        }

        /// <summary>
        ///  Serializes object from C#-object to XML File.
        /// </summary>
        /// <param name="obj">the object to serialize</param>
        /// <returns>the path of the new XML File</returns>
        private static String SerializeObject(Object obj)
        {
            // path is always XMLFiles/<typeofObj>.xml; all XML Files are placed in this folder
            String objXMLPath = "objectCache.xml"; // Achtung: sicherstellen, dass nur ein gleichzeitiger Zugriff auf Datei erfolgt, weil es nur eine Datei gibt, die alle XML Serialisierungen enthält
            XmlSerializer xmlser = new XmlSerializer(obj.GetType());

            FileStream fs = new FileStream("../../XMLFiles/" + objXMLPath, FileMode.Create);
            xmlser.Serialize(fs, obj);
            fs.Close();
            return "../../XMLFiles/" + objXMLPath;
        }
    }
}