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
    public class CommunicationExceptionTest2
    {
        public static RestRequester myRequester;
     
        [ClassInitialize()]
        public static void ClassInit(TestContext context)
        {
            // initialize admin client requester
            myRequester = new RestRequester("admin");
            myRequester.InitializeClientProperties("TestAdmin", "abc123");
        }

        /// <summary>
        /// Test the private method 'GetObjectRequest' in RestRequester. Tests the ExceptionHandling if server is not running.
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(WorkflowNotExistException))]
        public void testServerConnectionGetObject()
        {
            myRequester.GetObject<Workflow>("15");
        }

        /// <summary>
        /// Test the private method 'SendSimpleRequest' in RestRequester. Tests the ExceptionHandling if server is not running.
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(WorkflowNotExistException))]
        public void testServerConnectionSendSimpleReq()
        {
            Role testRole = new Role();
            testRole.rolename = "Testrole";

            // one User
            User testUser = new User();
            testUser.username = "Rahel";
            testUser.roles.Add(testRole);

            // one Workflow
            Workflow newWf = new Workflow();

            // a startStep
            StartStep startStep = new StartStep();
            startStep.roleIds.Add("Testrole");
            newWf.addStep(startStep);

            // an action
            Action act = new Action();
            act.roleIds.Add("Testrole");
            newWf.addStep(act);

            // a final step
            FinalStep fStep = new FinalStep();
            fStep.roleIds.Add("Testrole");
            newWf.addStep(fStep);

            myRequester.PostObject(testRole);
            myRequester.PostObject(testUser);
            myRequester.PostObject(newWf);

            myRequester.StepForward("1", "2");
        }

        /// <summary>
        /// Tests if the right Exception is thrown if a not existing Object is requested.
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(WorkflowNotExistException))]
        public void testWorkflowDoesntExistsException()
        {
            int getWFId = -17;
            myRequester.GetObject<Workflow>(getWFId.ToString());
        }
    }
}