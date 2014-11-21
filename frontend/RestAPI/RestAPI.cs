using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using RestSharp;

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

    public class Workflow
    {
            

    }


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
         **/
        public static void Main(string[] args)
        {
            RestRequester.init();
           
            Console.WriteLine("*** REST-Service ist " + RestRequester.restserverurl + " ***\n");
            Workflow result = RestRequester.getObject<Workflow>(1);
            Console.WriteLine("Get Anfrage erfolgreich geschickt... starte Del-Anfrage.");
            Workflow deleteRes = RestRequester.deleteObject<Workflow>(1);
            Console.ReadKey();
        }
        
    }

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
            String url = "items/" + typeName +"/" + id.ToString();
            O request = sendRequest<O>(url, Method.GET);

            return request;
        }

        /*
        * Update an object on the server, with HTTP-Method PUT.
        * Path for this HTTP-Method is always: update/<type>/<id>
        */
        public static O updateObject<O>(int id) where O : new()
        {
            String typeName = typeof(O).FullName.Split('.').Last().ToLower();
            String url = "update/" + typeName + "/" + id.ToString();
            O request = sendRequest<O>(url, Method.PUT);

            return request;
        }

        /*
        * Create an object on the server, with HTTP-Method POST.
        * Path for this HTTP-Method is always: send/<type>
        */
        public static O postObject<O>(int id) where O : new()
        {
            String typeName = typeof(O).FullName.Split('.').Last().ToLower();
            String url = "send/" + typeName;
            O request = sendRequest<O>(url, Method.POST);

            return request;
        }

        /*
        * Delete an object on the server, with HTTP-Method DEL.
        * Path for this HTTP-Method is always: delete/<type>/<id>
        */
        public static O deleteObject<O>(int id) where O : new()
        {
            String typeName = typeof(O).FullName.Split('.').Last().ToLower();
            String url = "delete/" + typeName + "/" + id.ToString();
            O request = sendRequest<O>(url, Method.DELETE);

            return request;
        }

        /**
         * Sends a HTTP-Request to the REST-Server. 
         * 
         * @url - the requested url
         * @method - the requested HTTP-Method
         */
        private static O sendRequest<O>(String url, RestSharp.Method method) where O : new()
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
                var response = client.Execute<OwnException>(request);
                throw response.Data;
            }
        }
    }
}