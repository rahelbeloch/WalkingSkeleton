using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using RestSharp;
using System.Web;

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
            O request = SendObjectRequest<O>(url, Method.GET, null);

            return request;
        }

        /*
        * Update an object on the server, with HTTP-Method PUT.
        * Path for this HTTP-Method is always: update/<type>/<id>
        */
        public static O updateObject<O>(int id) where O : new()
        {
            String typeName = typeof(O).FullName.Split('.').Last().ToLower();
            String url = "update/" + typeName + "/" + id;
            O request = SendObjectRequest<O>(url, Method.PUT, null);

            return request;
        }

        /*
        * Create an object on the server, with HTTP-Method POST.
        * Path for this HTTP-Method is always: send/<type>
        */
        public static O postObject<O>(String serializedObj) where O : new()
        {
            String typeName = typeof(O).FullName.Split('.').Last().ToLower();
            String url = "send/" + typeName;

            O request = SendObjectRequest<O>(url, Method.POST, serializedObj);

            return request;
        }

        /*
        * Delete an object on the server, with HTTP-Method DEL.
        * Path for this HTTP-Method is always: delete/<type>/<id>
        */
        public static O deleteObject<O>(int id) where O : new()
        {
            String typeName = typeof(O).FullName.Split('.').Last().ToLower();
            String url = "delete/" + typeName + "/" + id;
            O request = SendObjectRequest<O>(url, Method.DELETE, null);

            return request;
        }

        /// <summary>
        ///     Sends a request to the server to start a workflow (create an item)
        /// </summary>
        /// <param name="wId">Workflow-Id</param>
        /// <param name="uId">User-Id</param>
        public static void StartWorkflow(int wId, int uId)
        {
            // start/workflowid/userid
            String url = "start/" + wId + "/" + uId;
            IRestResponse response = SendSimpleRequest(url, Method.POST);
            // do something with the response, e.g. look if everything is ok.
        }

        /// <summary>
        ///  
        /// </summary>
        /// <param name="stepId"></param>
        /// <param name="itemId"></param>
        /// <param name="uId"></param>
        public static void StepForward(int stepId, int itemId, int uId)
        {
            // forward/stepid/itemid/userid
            String url = "forward/" + stepId + "/" + itemId + "/" + uId;
            IRestResponse response = SendSimpleRequest(url, Method.POST);
            // do something with the response, e.g. look if everything is ok.
        }

        /**
         * Sends a HTTP-Request to the REST-Server. 
         * 
         * @url - the requested url
         * @method - the requested HTTP-Method
         */
        private static O SendObjectRequest<O>(String url, RestSharp.Method method, String serializedObj) where O : new()
        {
            Console.WriteLine("requested Url -> " + restserverurl + url);
            var request = new RestRequest(url, method);

            // if there is an object to send to server per XML
            if (serializedObj != null)
            {
                request.RequestFormat = RestSharp.DataFormat.Xml;
                request.AddParameter("text/xml", serializedObj, ParameterType.RequestBody);
            }

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

    }
}