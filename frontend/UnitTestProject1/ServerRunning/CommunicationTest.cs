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

        [ClassInitialize()]
        public static void ClassInit(TestContext context)
        {
        }

        /// <summary>
        ///  Testmethod to test a transmission of a workflow to server.
        /// </summary>
        [TestMethod]
        public void testSentWorkflow()
        {
            // workflow with ID 1
            Workflow testWf = new Workflow();

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
            User testUser = new User();
            testUser.username = "Rahel";

            Boolean postUser = InternalRequester.PostObject<User>(testUser);

            Boolean send = InternalRequester.PostObject<Workflow>(testWf);
            
            Assert.IsTrue(send == true);

        }


        /// <summary>
        ///  Test to start a Workflow.
        /// </summary>
        [TestMethod]
        public void testStartWorkflow()
        {
            Boolean done = InternalRequester.StartWorkflow(1, "Rahel");

            Assert.IsTrue(done);
        }

        /// <summary>
        ///     Test to switch forward some actions.
        /// </summary>
        [TestMethod]
        public void testSwitchForward()
        {
            Workflow stepWf = InternalRequester.GetObject<Workflow>(1);
            int stepId = stepWf.steps[0].id;
            int itemId = stepWf.items[0].id;
            string username = "Rahel";

            Boolean done = InternalRequester.StepForward(stepId, itemId, username);

            Assert.IsTrue(done);
        }

        /// <summary>
        ///     Test to switch forward some actions.
        /// </summary>
        [TestMethod]
        public void testUpdateObject()
        {
            Workflow changeWorkflow = InternalRequester.GetObject<Workflow>(1);
            int stepCount = changeWorkflow.steps.Count;
            changeWorkflow.addStep(new Action());

            InternalRequester.UpdateObject(changeWorkflow);

            Workflow updatedWorkflow = InternalRequester.GetObject<Workflow>(1);

            int newStepCount = updatedWorkflow.steps.Count;

            Assert.IsTrue(stepCount + 1 == newStepCount);
        }

        /// <summary>
        ///     Test deleting a workflow.
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

            User testUser = new User();
            testUser.username = "Sebastian";

            Workflow newWf = new Workflow();
            
            // a startStep
            StartStep startStep = new StartStep();
            startStep.username = "Sebastian";
            newWf.addStep(startStep);

            // an action
            Action act = new Action();
            act.username = "Sebastian";
            newWf.addStep(act);

            // a final step
            FinalStep fStep = new FinalStep();
            fStep.username = "Sebastian";
            newWf.addStep(fStep);

            

            InternalRequester.PostObject<User>(testUser);

            InternalRequester.PostObject<Workflow>(newWf);


            IList<Workflow> eleList = InternalRequester.GetAllObjects<Workflow>("Sebastian");
            System.Diagnostics.Trace.WriteLine("Anzahl Workflows Sebastian:" + eleList.Count());
            Assert.IsTrue(eleList.Count == 1);
        }
        /*
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

            Boolean done = RestRequester.checkUser(testUser.username, testpwd);
          
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
        }
         */
    }
}