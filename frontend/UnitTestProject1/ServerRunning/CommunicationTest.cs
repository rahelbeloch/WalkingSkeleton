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



namespace UnitTestProject1
{
    /// <summary>
    /// Test class for workflow (backing) bean logic.
    /// </summary>
    [TestClass]
    public class CommunicationTest
    {

        static Workflow testWf;
        static User testUser;

        [ClassInitialize()]
        public static void ClassInit(TestContext context)
        {
            // workflow with ID 1
            testWf = new Workflow();
            
            // a startStep
            StartStep startStep = new StartStep();
            startStep.username = "Rahel";
            testWf.addStep(startStep);

            // an action
            Action act = new Action();
            act.username = "Rahel";
            testWf.addStep(act);

            // a final step
            FinalStep fStep = new FinalStep();
            fStep.username = "Rahel";
            testWf.addStep(fStep);

            // First User with name "Rahel"
            testUser = new User();
            testUser.username = "Rahel";

            RestRequester.PostObject<User>(testUser);
        }

        /// <summary>
        ///  Testmethod to test a transmission of a workflow to server.
        /// </summary>
        [TestMethod]
        public void testSentWorkflow()
        {
            Boolean send = RestRequester.PostObject<Workflow>(testWf);
            
            Assert.IsTrue(send == true);
        }


        /// <summary>
        ///  Test to start a Workflow.
        /// </summary>
        [TestMethod]
        public void testStartWorkflow()
        {
            RestRequester.PostObject<Workflow>(testWf);

            Boolean done = RestRequester.StartWorkflow(1, testUser.username);

            Assert.IsTrue(done);
        }

        /// <summary>
        ///     Test to switch forward some actions.
        /// </summary>
        [TestMethod]
        public void testSwitchForward()
        {
            int stepId = 5;
            int itemId = 11;
            string username = "Rahel";

            Boolean done = RestRequester.StepForward(stepId, itemId, username);

            Assert.IsTrue(done);
        }

        /// <summary>
        ///     Test to switch forward some actions.
        /// </summary>
        [TestMethod]
        public void testUpdateObject()
        {
            Workflow changeWorkflow = RestRequester.GetObject<Workflow>(1);
            int stepCount = changeWorkflow.steps.Count;
            changeWorkflow.addStep(new Action());

            RestRequester.UpdateObject(changeWorkflow);

            Workflow updatedWorkflow = RestRequester.GetObject<Workflow>(1);

            int newStepCount = updatedWorkflow.steps.Count;

            Assert.IsTrue(stepCount + 1 == newStepCount);
        }

        /// <summary>
        ///     Test to switch forward some actions.
        /// </summary>
        [TestMethod]
        public void testDeleteObject()
        {
        }

        /// <summary>
        ///     Test to get a list of objects from server.
        /// </summary>
        [TestMethod]
        public void testGetAllObjects()
        {
            IList<Workflow> eleList = RestRequester.GetAllObjects<Workflow>("Rahel");

            Assert.IsTrue(eleList.Count == 3);
        }

        /// <summary>
        ///     Test to get a list of objects from server.
        /// </summary>
        [TestMethod]
        public void testCheckExistentUser()
        {
            string initString = "TestPasswort";
            SecureString testpwd = new SecureString();
            foreach (char ch in initString)
               testpwd.AppendChar(ch);

            Boolean done = RestRequester.checkUser("Rahel", testpwd);
          
            Assert.IsTrue(done);
        }

        /// <summary>
        ///     Test to get a list of objects from server.
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(UserNotExistException))]
        public void testCheckNotExistentUser()
        {
            string initString = "TestPasswort";
            SecureString testpwd = new SecureString();
            foreach (char ch in initString)
                testpwd.AppendChar(ch);

            Boolean done = RestRequester.checkUser("Jane", testpwd);

            Assert.IsTrue(done);
        }
    }
}