using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using CommunicationLib.Model;
using RestAPI;
using RestSharp;
using System.Web;
using System.IO;
using System.Security;
using CommunicationLib.Exception;
using Action = CommunicationLib.Model.Action;
using System.Diagnostics;
using CommunicationLib;


namespace UnitTestProject1
{
   /// <summary>
    /// Test class for Rest API Testing. ATTENTION: Testing only is designed for usage with a running server. 
    /// </summary>
    [TestClass]
    public class CommunicationTestAdmin
    {
        // local object of connection to server
        public static RestRequester myRequester;
        // Logger
        public static TextWriterTraceListener myListener;

        /// <summary>
        /// Init method, does instantiate the Connection Instance.
        /// </summary>
        /// <param name="context"></param>
        [ClassInitialize()]
        public static void ClassInit(TestContext context)
        {
            // initialize admin client requester
            myRequester = new RestRequester("admin", "htp://localhost:18887");

            // Some Loggings
            myListener = new TextWriterTraceListener("../../CommunicationTestLog.log", "myListener");
            System.IO.File.WriteAllBytes("../../CommunicationTestLog.log", new byte[0]);
        }

        /// <summary>
        ///     Test if a on server existent user can login.
        /// </summary>
        [TestMethod]
        public void testCheckExistentUserLogIn()
        {
            myRequester.InitializeClientProperties("TestAdmin", "abc123");
            Boolean done = myRequester.CheckUser();

            Assert.IsTrue(done);
        }

        /// <summary>
        ///     Test if the right exception is thrown if a non-existent user logs in.
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(LogInException))]
        public void testCheckNotExistentUserLogIn()
        {
            myRequester.InitializeClientProperties("NotExistentUser", "password");
            Boolean done = myRequester.CheckUser();
        }
    }
}