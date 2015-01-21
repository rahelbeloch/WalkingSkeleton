using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using CommunicationLib.Exception;
using Action = CommunicationLib.Model.Action;
using RestAPI;
using CommunicationLib.Model;
using System.Security;

namespace UnitTestProject1
{
    /// <summary>
    /// Class to test the connection to the server. There are three private methods in RestRequester, who communicate
    /// with the server. It is sufficient to call three public methods, who call the three private methods to test the connection integral.
    /// </summary>
    [TestClass]
    public class CommunicationExceptionTest1
    {
        public static RestRequester myRequester;

        [ClassInitialize()]
        public static void ClassInit(TestContext context)
        {
            // initialize admin client requester
            myRequester = new RestRequester("admin", "htp://localhost:18887");
            myRequester.InitializeClientProperties("TestAdmin", "abc123");
        }

        /// <summary>
        /// Test the private method 'GetObjectRequest' in RestRequester. Tests the ExceptionHandling if server is not running.
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(ServerNotRunningException))]
        public void testServerConnectionGetObject()
        {
            myRequester.GetObject<Workflow>("0");
        }

        /// <summary>
        /// Test the private method 'GetObjectRequest' in RestRequester. Tests the ExceptionHandling if server is not running.
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(ServerNotRunningException))]
        public void testServerConnectionSendObjectRequest()
        {
           myRequester.PostObject(new Workflow());
        }

        /// <summary>
        /// Test the private method 'SendSimpleRequest' in RestRequester. Tests the ExceptionHandling if server is not running.
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(ServerNotRunningException))]
        public void testServerConnectionSendSimpleReq()
        {
            myRequester.StepForward("1", "2");
        }
    }
}