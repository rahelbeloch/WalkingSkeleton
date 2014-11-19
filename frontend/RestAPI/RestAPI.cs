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


    public class RestRequester
    {

        static String restserverurl = "http://172.26.38.109:8080/";
        static RestClient client;

        public static void init()
        {
            client = new RestClient(restserverurl);
        }

        /*
         * Public supported methods by this API.
         */
        public static O getObject<O>(int id) where O : new()
        {
            String url = "/" + typeof(O).FullName + "/" + id.ToString();
            O request = sendRequest<O>(url, Method.GET);

            return request;
        }

        public static O updateObject<O>(int id) where O : new()
        {
            String url = "/" + typeof(O).FullName + "/" + id.ToString();
            O request = sendRequest<O>(url, Method.PUT);

            return request;
        }

        public static O postObject<O>(int id) where O : new()
        {
            String url = "/" + typeof(O).FullName + "/" + id.ToString();
            O request = sendRequest<O>(url, Method.POST);

            return request;
        }

        public static O deleteObject<O>(int id) where O : new()
        {
            String url = "/" + typeof(O).FullName + "/" + id.ToString();
            O request = sendRequest<O>(url, Method.DELETE);

            return request;
        }


        private static O sendRequest<O>(String url, RestSharp.Method method) where O : new()
        {
            var request = new RestRequest(restserverurl + url, method);
            
            // decide wether the server does return the right excepted object or throws an exception
            try
            {
                var response = client.Execute<O>(request);
                O obj = response.Data;

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