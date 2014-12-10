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
    ///     Static class, that realizes the Connection to the server.
    /// </summary>
    public class InternalRequester
    {
        public static String restserverurl;
        public static RestClient client;

        ///<summary>
        ///     Static Constructor - is called automatically at first use of the class.
        /// </summary>
        static InternalRequester()
        {
            restserverurl = Constants.serverUrl;
            client = new RestClient(restserverurl);
        }

       /// <summary>
        /// Requests all Objects (Items, Workflows or Users) belonging to the given user.
       /// </summary>
       /// <typeparam name="O"></typeparam>
       /// <param name="userName"></param>
       /// <returns></returns>
        internal static IRestResponse GetAllObjects<O>(String url, RestSharp.Method method)
        {
            IRestResponse response;

            var request = new RestRequest(url, Method.GET);
            request.AddHeader("Accept", "text/plain");

            // decide wether the server does return the right excepted object or throws an exception
            response = client.Execute(request);

            // if there is a network transport error (network is down, failed DNS lookup, etc)
            if (response.ResponseStatus == ResponseStatus.Error)
            {
                ConnectionException ex = new ServerNotRunningException();
                throw ex;
            }

            // if the statusCode is 500 (Error) there happened an error on the server
            if (response.StatusCode != HttpStatusCode.OK && response.StatusCode == HttpStatusCode.InternalServerError)
            {
                int errorCode = Int32.Parse(response.Content);
                BasicException ex = (BasicException)Activator.CreateInstance(ErrorMessageMapper.GetErrorType(errorCode));
                throw ex;
            }

            return response;
        }

        

        /// <summary>
        ///     Sends a HTTP-Request to the server to get an object.
        /// </summary>
        /// <typeparam name="O">Type of the requested object</typeparam>
        /// <param name="url">Requested url</param>
        /// <param name="method">HTTP-Method of the request</param>
        /// <returns>Response object from server</returns>
        internal static IRestResponse GetObjectRequest<O>(String url, RestSharp.Method method) where O : new()
        {
            var request = new RestRequest(url, method);
            request.AddHeader("Accept", "text/plain");
            IRestResponse response;

            // decide wether the server does return the right excepted object or throws an exception
            response = client.Execute(request);

            // if there is a network transport error (network is down, failed DNS lookup, etc)
            if (response.ResponseStatus == ResponseStatus.Error)
            {
                ConnectionException ex = new ServerNotRunningException();
                throw ex;
            }

            // test the StatusCode of response; if 500 happened there is a Server Error
            if (response.StatusCode != HttpStatusCode.OK && response.StatusCode == HttpStatusCode.InternalServerError)
            {
                System.Diagnostics.Trace.WriteLine("Antwort: " + response.Content);
                int errorCode = Int32.Parse(response.Content);
                BasicException ex = (BasicException)Activator.CreateInstance(ErrorMessageMapper.GetErrorType(errorCode));
                throw ex;
            }
            return response;
        }

        /// <summary>
        ///     Method to send a C# Object to server.
        /// </summary>
        /// <param name="url">Request url</param>
        /// <param name="method">Method of the request</param>
        /// <param name="serializedObjPath">JSON string serialized object</param>
        /// <returns>The response from server</returns>
        internal static IRestResponse SendObjectRequest(String url, RestSharp.Method method, String serializedObj)
        {
            IRestResponse response;
            var request = new RestRequest(url, method);
            request.AddHeader("Accept", "text/plain");

            // if there is an object that shall be send to server
            if (serializedObj != null)
            {
                request.RequestFormat = RestSharp.DataFormat.Json;
                request.AddParameter("data", serializedObj, ParameterType.GetOrPost);
            }

            // decide wether the server does return the right excepted object or throws an exception
            response = client.Execute(request);

            // if there is a network transport error (network is down, failed DNS lookup, etc)
            if (response.ResponseStatus == ResponseStatus.Error)
            {
                ConnectionException ex = new ServerNotRunningException();
                throw ex;
            }

            if (response.StatusCode != HttpStatusCode.OK && response.StatusCode == HttpStatusCode.InternalServerError)
            {
                System.Diagnostics.Trace.WriteLine("response: " + response);
                System.Diagnostics.Trace.WriteLine("response.Content " + response.Content);
                int errorCode = Int32.Parse(response.Content);
                BasicException ex = (BasicException)Activator.CreateInstance(ErrorMessageMapper.GetErrorType(errorCode));
                System.Diagnostics.Trace.WriteLine("errorCode: " + errorCode);
                throw ex;
            }
            return response;
        }

        /// <summary>
        ///     Sends a simple request, just containing some information in the url. No more parameters or objects send in the request.
        /// </summary>
        /// <param name="request">The request to send to server</param>
        /// <returns>The response object</returns>
        internal static IRestResponse SendSimpleRequest(RestRequest request)
        {
            IRestResponse response = client.Execute(request);

            // if there is a network transport error (network is down, failed DNS lookup, etc)
            if (response.ResponseStatus == ResponseStatus.Error)
            {
                ConnectionException ex = new ServerNotRunningException();
                throw ex;
            }

            // if no HttpException happened and although the StatusCode is not "OK", there must be on Exception of our own
            if (response.StatusCode != HttpStatusCode.OK && response.StatusCode == HttpStatusCode.InternalServerError)
            {
                int errorCode = Int32.Parse(response.Content);
                BasicException ex = (BasicException)Activator.CreateInstance(ErrorMessageMapper.GetErrorType(errorCode));
                System.Diagnostics.Trace.WriteLine("errorCode: " + errorCode);
                throw ex;
            }
            return response;
        }
    }
}