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
using System.Diagnostics;
using NLog;

namespace RestAPI
{
    /// <summary>
    ///     Static class, that realizes the internal connection to the server.
    /// </summary>
    public class InternalRequester
    {
        public static RestClient client;
        private static Logger logger = LogManager.GetCurrentClassLogger();

        ///<summary>
        ///     Static Constructor - is called automatically at first use of the class.
        /// </summary>
        static InternalRequester()
        {
            client = new RestClient(Constants.SERVER_URL);
        }

        /// <summary>
        ///     Sends a simple request, request is given.
        /// </summary>
        /// <param name="request">The request to send to server</param>
        /// <returns>The response object</returns>
        internal static IRestResponse RetrieveRequest(RestRequest request)
        {
            // execute the request
            IRestResponse response = client.Execute(request);
            logger.Info(" resp " + response.ErrorException);
            logger.Info(" resp " + response.ErrorMessage);
            logger.Info(" resp " + response.StatusCode);
            try
            {
                ProofResponseErrors(response);
            }
            catch (BasicException)
            {
                throw;
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
        internal static IRestResponse SendRequest(RestRequest request, String serializedObj)
        {
            IRestResponse response;
            // if there is an object that shall be send to server
            if (serializedObj != null)
            {
                request.RequestFormat = RestSharp.DataFormat.Json;
                request.AddParameter("data", serializedObj, ParameterType.GetOrPost);
            }

            // execute the request
            response = client.Execute(request);

            try
            {
                ProofResponseErrors(response);
            }
            catch (BasicException)
            {
                throw;
            }

            return response;
        }

        /// <summary>
        /// Method inspects the response for server or connection errors. If an error happens, it throws the adapted exception to its caller.
        /// </summary>
        /// <param name="response">The response of interest</param>
        private static void ProofResponseErrors(IRestResponse response)
        {
            // if there is a network transport error (network is down, failed DNS lookup, etc)
            if (response.ResponseStatus == ResponseStatus.Error)
            {
                ConnectionException ex = new ServerNotRunningException();
                throw ex;
            }

            // if no HttpException happened and although the StatusCode is not "OK", there must be on Exception of our own
            if (response.StatusCode != HttpStatusCode.OK && response.StatusCode == HttpStatusCode.InternalServerError)
            {
                logger.Warn("!!!!!! " + response.Content);
                int errorCode = Int32.Parse(response.Content);

                //generate convenient exception
                BasicException ex = (BasicException)Activator.CreateInstance(ErrorMessageMapper.GetErrorType(errorCode));
                throw ex;
            }
        }
    }
}