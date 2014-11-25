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

    /**
     *  Program class to test the connection. 
     
    public class Programm
    {

        static void printResponse(string info, IRestResponse resp)
        {
            string pfx = "[" + info + "] ";
            Console.WriteLine(pfx + "Antwort Status: " + resp.ResponseStatus);
            Console.WriteLine(pfx + "Empfange Antwort als " + resp.ContentType);
            Console.WriteLine(pfx + "Antwort = " + resp.Content);
            Console.WriteLine();
        }

        /**
         * Method to test the Connection
         
        public static void Main(string[] args)
        {
            RestRequester.init();
           
            Console.WriteLine("*** REST-Service ist " + RestRequester.restserverurl + " ***\n");
            Workflow result = RestRequester.getObject<Workflow>(1);
            Console.WriteLine("Get Anfrage erfolgreich geschickt... starte Del-Anfrage.");
            Workflow deleteRes = RestRequester.deleteObject<Workflow>(1);
            Console.ReadKey();
        }
        
    }*/

    /*
     * Static class, that realizes the Connection to the server.
     */
    public class RestRequester
    {
        public static String restserverurl;
        static RestClient client;

        /**
         * Initializes the RestClient. Has to be called before first use.
         */
        public static void init()
        {
            restserverurl = "http://172.26.38.105:8080/";
            client = new RestClient(restserverurl);
        }

        /*
         * Get an object from the server, with HTTP-Method GET.
         * Path for this HTTP-Method is always: items/<type>/<id>
         */
        public static O getObject<O>(int id) where O : new()
        {
            String typeName = typeof(O).FullName.Split('.').Last().ToLower();
            String url = "items/" + typeName +"/" + id;
            O request = GetObjectRequest<O>(url, Method.GET);

            return request;
        }

        /*
        * Update an object on the server, with HTTP-Method PUT.
        * Path for this HTTP-Method is always: update/<type>
        */
        public static String updateObject(Object sendObj)
        {
            String typeName = sendObj.GetType().FullName.Split('.').Last().ToLower();
            String url = "update/" + typeName;
            String serializedObjPath = SerializeObject(sendObj);
            IRestResponse request = SendObjectRequest(url, Method.PUT, serializedObjPath);

            return request.Content;
        }

        /*
        * Create an object on the server, with HTTP-Method POST.
        * Path for this HTTP-Method is always: send/<type>
        */
        public static String postObject<O>(Object sendObj) where O : new()
        {
            String typeName = typeof(O).FullName.Split('.').Last().ToLower();
            String url = "send/" + typeName;

            String serializedObjPath = SerializeObject(sendObj);
            IRestResponse request = SendObjectRequest(url, Method.POST, serializedObjPath);

            return request.Content;
        }

        /*
        * Delete an object on the server, with HTTP-Method DEL.
        * Path for this HTTP-Method is always: delete/<type>/<id>
        */
        public static O deleteObject<O>(int id) where O : new()
        {
            String typeName = typeof(O).FullName.Split('.').Last().ToLower();
            String url = "delete/" + typeName + "/" + id;
            O request = GetObjectRequest<O>(url, Method.DELETE);

            return request;
        }

        /// <summary>
        ///     Sends a request to the server to start a workflow (create an item)
        /// </summary>
        /// <param name="wId">Workflow-Id</param>
        /// <param name="uId">User-Id</param>
        public static Boolean StartWorkflow(int wId, int uId)
        {
            // start/workflowid/userid
            String url = "start/" + wId + "/" + uId;
            IRestResponse response = SendSimpleRequest(url, Method.POST);
            // do something with the response, e.g. look if everything is ok.
            return true;
        }

        /// <summary>
        ///     Sends a state change of an action to the server
        /// </summary>
        /// <param name="stepId">id of the current step</param>
        /// <param name="itemId">id of the current item</param>
        /// <param name="uId">id of the current user</param>
        public static Boolean StepForward(int stepId, int itemId, int uId)
        {
            // Request url: 'forward/stepid/itemid/userid'
            String url = "forward/" + stepId + "/" + itemId + "/" + uId;
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
            Console.WriteLine("requested Url -> " + restserverurl + url);
            var request = new RestRequest(url, method);

            // decide wether the server does return the right excepted object or throws an exception
            try
            {
                var response = client.Execute<O>(request);
                O obj = response.Data;

                Console.WriteLine("Answer from Server -> " + obj);

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

            // if there is an object-path to a xml file that shall be send to server per XML
            if (serializedObjPath != null)
            {
                request.RequestFormat = RestSharp.DataFormat.Xml;
                request.AddParameter("text/xml", File.ReadAllText(serializedObjPath), ParameterType.RequestBody);
            }

            // decide wether the server does return the right excepted object or throws an exception
            try
            {
                var response = client.Execute(request);
                Console.WriteLine("Answer from Server -> " + response);
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
            String objXMLPath = obj.GetType() + ".xml";
            XmlSerializer xmlser = new XmlSerializer(obj.GetType());
            xmlser.Serialize(new FileStream("/XMLFiles/" + objXMLPath,FileMode.Create), obj);
            return "/XMLFiles/" + objXMLPath;
        }
    }
}