using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using CommunicationLib.Exception;
using Action = CommunicationLib.Model.Action;
using RestAPI;
using CommunicationLib.Model;

namespace UnitTestProject1
{
    /// <summary>
    /// Class to test the connection to the server. There are three private methods in RestRequester, who communicate
    /// with the server. It is sufficient to call three public methods, who call the three private methods to test the connection integral.
    /// </summary>
    [TestClass]
    public class CommunicationExceptionTest2
    {
        /// <summary>
        /// Test the private method 'GetObjectRequest' in RestRequester. Tests the ExceptionHandling if server is not running.
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(WorkflowNotExistException))]
        public void testServerConnectionGetObject()
        {
            InternalRequester.GetObject<Workflow>(0);
        }

        /// <summary>
        /// Test the private method 'SendSimpleRequest' in RestRequester. Tests the ExceptionHandling if server is not running.
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(ItemNotExistException))]
        public void testServerConnectionSendSimpleReq()
        {
            InternalRequester.StepForward(1, 2, "Rahel");
        }

        /// <summary>
        /// Tests if the right Exception is thrown if a not existing Object is requested.
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(WorkflowNotExistException))]
        public void testWorkflowDoesntExistsException()
        {
            int getWFId = -17;
            InternalRequester.GetObject<Workflow>(getWFId);
        }
    }
}
