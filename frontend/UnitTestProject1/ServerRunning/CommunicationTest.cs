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

namespace UnitTestProject1
{
    /// <summary>
    /// Test class for Rest API Testing. ATTENTION: Testing only is designed for usage with a running server. 
    /// </summary>
    [TestClass]
    public class CommunicationTest
    {
        // local object of connection to server
        public static RestRequester myRequester;

        /// <summary>
        /// Init method, does instantiate the Connection Instance.
        /// </summary>
        /// <param name="context"></param>
        [ClassInitialize()]
        public static void ClassInit(TestContext context)
        {
            myRequester = new RestRequester();
        }

        /// <summary>
        ///  Testmethod to test a transmission of a workflow to server.
        /// </summary>
        [TestMethod]
        public void testSentWorkflow()
        {
            // generate some TestData
            string username = "Rahel";
            Boolean send = generateTestUserAndWorkflow(username);
            IList<Workflow> eleList = myRequester.GetAllWorkflowsByUser(username);
            Workflow wf = eleList[0];
            
            Assert.IsTrue(send == true);

            // Clean up the stuff
            myRequester.DeleteObject<Workflow>(wf.id);
            myRequester.DeleteUser(username);
        }

        /// <summary>
        ///  Test to start a Workflow.
        /// </summary>
        [TestMethod]
        public void testStartWorkflow()
        {
            // generate some TestData
            string username = "Alex";
            generateTestUserAndWorkflow(username);
            IList<Workflow> eleList = myRequester.GetAllWorkflowsByUser(username);
            Workflow wf = eleList[0];

            // Test the real functionality
            Boolean done = myRequester.StartWorkflow(wf.id, username);

            Assert.IsTrue(done);

            // Clean up the stuff
            // ATTENTION: Workflow and user can not be deleted, because WF is already startet. Functionality realised in Sprint 2.
            //myRequester.DeleteObject<Workflow>(wf.id);
            //myRequester.DeleteUser(username);
        }

        /// <summary>
        ///     Test to switch forward some actions.
        /// </summary>
        [TestMethod]
        public void testSwitchForward()
        {
            // generate some TestData
            string username = "Dominik";
            generateTestUserAndWorkflow(username);
            IList<Workflow> eleList = myRequester.GetAllWorkflowsByUser(username);

            Workflow wf = eleList[0];
            myRequester.StartWorkflow(wf.id, username);

            // Test the real functionality
            Workflow stepWf = myRequester.GetObject<Workflow>(wf.id);
            
            // Some Loggings
            TextWriterTraceListener myListener = new TextWriterTraceListener("../../TextWriterOutput.log", "myListener");
            myListener.WriteLine("Anzahl der Steps: " + stepWf.steps.Count());
            myListener.WriteLine("Anzahl der Items: " + stepWf.steps.Count());
            // You must close or flush the trace listener to empty the output buffer.
            myListener.Flush();
            
            int stepId = stepWf.steps[0].id;
            int itemId = stepWf.items[0].id;

            Boolean done = myRequester.StepForward(stepId, itemId, username);
            myListener.WriteLine("Gwforwarded " + done);

            Assert.IsTrue(done);
           
            // Clean up the stuff
            // ATTENTION: Workflow and user can not be deleted, because WF is already startet. Functionality realised in Sprint 2.
            //myRequester.DeleteObject<Workflow>(wf.id);
            //myRequester.DeleteUser(username);
        }

        /// <summary>
        ///     Test to update a workflow.
        /// </summary>
        [TestMethod]
        public void testUpdateObject()
        {
            // generate some TestData
            generateTestUserAndWorkflow("Elizabeth");
            IList<Workflow> eleList = myRequester.GetAllWorkflowsByUser("Elizabeth");
            Workflow wf = eleList[0];

            // Testing the funcionality
            Workflow changeWorkflow = myRequester.GetObject<Workflow>(wf.id);
            int stepCount = changeWorkflow.steps.Count;
            changeWorkflow.addStep(new Action());

            myRequester.UpdateObject(changeWorkflow);

            Workflow updatedWorkflow = myRequester.GetObject<Workflow>(wf.id);

            int newStepCount = updatedWorkflow.steps.Count;

            Assert.IsTrue(stepCount + 1 == newStepCount);

            // Clean up the stuff
            myRequester.DeleteObject<Workflow>(wf.id);
            myRequester.DeleteUser("Elizabeth");
        }

        /// <summary>
        ///     Test deleting a workflow. (Not implemented yet, because functionality is implemented in Sprint 2)
        /// </summary>
        [TestMethod]
        public void testDeleteObject()
        {
        }

        /// <summary>
        ///     Test to get a list of workflows of one user from server.
        /// </summary>
        [TestMethod]
        public void testGetAllWorkflowsByUser()
        {
            // generate some TestData
            generateTestUserAndWorkflow("Sebastian");
            
            IList<Workflow> eleList = myRequester.GetAllWorkflowsByUser("Sebastian");
            
            Assert.IsTrue(eleList.Count == 1);

            // Clean up the whole stuff
            Workflow wf = eleList[0];
            myRequester.DeleteObject<Workflow>(wf.id);
            myRequester.DeleteUser("Sebastian");
        }

         /// <summary>
        ///     Test to get a list of all workflows from server.
        /// </summary>
        [TestMethod]
        public void testGetAllWorkflows()
        {
            // generate some TestData
            generateTestUserAndWorkflow("Jerome");
            // generate some TestData
            generateTestUserAndWorkflow("Tilman");
            // generate some TestData
            generateTestUserAndWorkflow("Simon");

            IList<Workflow> wFList = myRequester.GetAllWorkflows();

            // Some Loggings
            TextWriterTraceListener myListener = new TextWriterTraceListener("../../TextWriterOutput3.log", "myListener");
            myListener.WriteLine("Anzahl der workflows: " + wFList.Count());
            // You must close or flush the trace listener to empty the output buffer.
            myListener.Flush();

            Assert.IsTrue(wFList.Count() == 4);

            // Cleanup --> missing
        }
        
        /// <summary>
        ///     Test if a on server existent user can login.
        /// </summary>
        [TestMethod]
        public void testCheckExistentUser()
        {
            User newUser = new User();
            newUser.username = "MaxMustermann";
            myRequester.PostObject<User>(newUser);

            string initString = "TestPasswort";
            SecureString testpwd = new SecureString();
            foreach (char ch in initString)
               testpwd.AppendChar(ch);

            Boolean done = myRequester.checkUser("MaxMustermann", testpwd);
          
            Assert.IsTrue(done);
        }

        /// <summary>
        ///     Test if the right exception is thrown if a non-existent user logs in.
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(UserNotExistException))]
        public void testCheckNotExistentUser()
        {
            string initString = "TestPasswort";
            SecureString testpwd = new SecureString();
            foreach (char ch in initString)
                testpwd.AppendChar(ch);

            Boolean done = myRequester.checkUser("Jane", testpwd);
        }
        
        /// <summary>
        ///   Generate some TestData for TestMethods.
        /// </summary>
        /// <param name="username"></param>
        /// <returns></returns>
        private Boolean generateTestUserAndWorkflow(string username)
        {
            // one User
            User testUser = new User();
            testUser.username = username;

            // one Workflow
            Workflow newWf = new Workflow();

            // a startStep
            StartStep startStep = new StartStep();
            startStep.username = username;
            newWf.addStep(startStep);

            // an action
            Action act = new Action();
            act.username = username;
            newWf.addStep(act);

            // a final step
            FinalStep fStep = new FinalStep();
            fStep.username = username;
            newWf.addStep(fStep);

            myRequester.PostObject<User>(testUser);
            myRequester.PostObject<Workflow>(newWf);

            return true;
        }
    }
}