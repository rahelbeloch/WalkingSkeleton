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
        private static RestClient client;
        private static Logger logger = LogManager.GetCurrentClassLogger();

        ///<summary>
        /// Constructor.
        /// </summary>
        /// <param name="serverAdress">the adress of the used server</param>
        public InternalRequester(string serverAdress)
        {
            if (serverAdress != null)
            {
                try
                {
                    client = new RestClient(serverAdress);
                }
                catch (UriFormatException)
                {
                    throw new ServerNotRunningException();
                }
            }
        }

        /// <summary>
        /// Refresh the rest client with new server url.
        /// </summary>
        /// <param name="serverAddress">the new server address</param>
        public void Refresh(string serverAddress)
        {
            try
            {
                client = new RestClient(serverAddress);
            }
            catch (UriFormatException)
            {
                throw new ServerNotRunningException();
            }
        }

        /// <summary>
        ///     Sends a simple request, request is given.
        /// </summary>
        /// <param name="request">The request to send to server</param>
        /// <returns>The response object</returns>
        internal IRestResponse RetrieveRequest(RestRequest request)
        {
            // execute the request
            IRestResponse response = client.Execute(request);
            logger.Info("StatusCode=" + response.StatusCode);

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
        /// <param name="request">the request object</param>
        /// <param name="serializedObj">JSON string serialized object</param>
        /// <returns>The response from server</returns>
        internal IRestResponse SendRequest(RestRequest request, String serializedObj)
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
        private void ProofResponseErrors(IRestResponse response)
        {
            // if there is a network transport error (network is down, failed DNS lookup, etc)
            if (response.ResponseStatus == ResponseStatus.Error)
            {
                ConnectionException ex = new ServerNotRunningException();
                throw ex;
            }

            // if no HttpException happened and although the StatusCode is not "OK", there must be on Exception of our own
            if (response.StatusCode == HttpStatusCode.InternalServerError)
            {
                logger.Info("ErrorCode=" + response.Content);
                int errorCode = Int32.Parse(response.Content);

                //generate convenient exception
                //BasicException ex = (BasicException)Activator.CreateInstance(ErrorMessageMapper.GetErrorType(errorCode));
                var ex = Activator.CreateInstance(ErrorMessageMapper.GetErrorType(errorCode));
                throw (Exception)ex;
            }
        }
    }
}