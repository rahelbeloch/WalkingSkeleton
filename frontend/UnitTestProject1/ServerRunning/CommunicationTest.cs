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
    public class CommunicationTest
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
            myRequester = new RestRequester("admin");

            // Some Loggings
            myListener = new TextWriterTraceListener("../../CommunicationTestLog.log", "myListener");
            System.IO.File.WriteAllBytes("../../CommunicationTestLog.log", new byte[0]);
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
            myRequester.InitializeClientProperties(username, "");
            IList<Workflow> eleList = myRequester.GetAllWorkflowsByUser();
            Workflow wf = eleList[0];
            
            Assert.IsTrue(send == true);

            // Clean up the stuff
            myRequester.DeleteObject<Workflow>(wf.id);
            myRequester.DeleteObject<User>(username);
        }

        /// <summary>
        ///  Test to start a Workflow.
        /// </summary>
        [TestMethod]
        public void testStartWorkflow()
        {
            // generate some TestData
            string username = "Melanie";
            generateTestUserAndWorkflow(username);
            myRequester.InitializeClientProperties("Melanie", "");

            IList<Workflow> eleList = myRequester.GetAllWorkflowsByUser();
            Workflow wf = eleList[0];

            // Test the real functionality
            Boolean done = myRequester.StartWorkflow(wf.id);

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
            string username = "Axel";
            // set testuser as client
            myRequester.InitializeClientProperties("Axel", "");
            generateTestUserAndWorkflow(username);

            // retrieve all workflows
            IList<Workflow> eleList = myRequester.GetAllWorkflowsByUser();

            // get the generated workflow
            Workflow wf = eleList[0];
            // start it
            myRequester.StartWorkflow(wf.id);

            // Test the real functionality
            Workflow stepWf = myRequester.GetObject<Workflow>(wf.id);
            
            myListener.WriteLine("Anzahl der Steps: " + stepWf.steps.Count());
            myListener.WriteLine("Anzahl der Items: " + stepWf.items.Count());
            // You must close or flush the trace listener to empty the output buffer.
            myListener.Flush();
            
            string stepId = stepWf.steps[0].id;
            string itemId = stepWf.items[0].id;

            Boolean done = myRequester.StepForward(stepId, itemId);
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
            myRequester.InitializeClientProperties("Elizabeth", "");
            generateTestUserAndWorkflow("Elizabeth");
           
            IList<Workflow> eleList = myRequester.GetAllWorkflowsByUser();
            Workflow wf = eleList[0];

            // Testing the funcionality
            Workflow changeWorkflow = myRequester.GetObject<Workflow>(wf.id);
            int stepCount = changeWorkflow.steps.Count;
            changeWorkflow.addStep(new FinalStep());

            myRequester.UpdateObject(changeWorkflow);

            Workflow updatedWorkflow = myRequester.GetObject<Workflow>(wf.id);

            int newStepCount = updatedWorkflow.steps.Count;
            myListener.WriteLine("Anzahl der Steps vorher: " + stepCount + " Anzahl nachher soll: " + changeWorkflow.steps.Count() + " Anzahl nachher ist: " + updatedWorkflow.steps.Count());
            myListener.Flush();

            Assert.IsTrue(stepCount + 1 == newStepCount);

            // Clean up the stuff
            myRequester.DeleteObject<Workflow>(wf.id);
            myRequester.DeleteObject<User>("Elizabeth");
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
            myRequester.InitializeClientProperties("Sebastian", "");
            
            IList<Workflow> eleList = myRequester.GetAllWorkflowsByUser();
            
            Assert.IsTrue(eleList.Count >= 1);

            // Clean up the whole stuff
            Workflow wf = eleList[0];
            myRequester.DeleteObject<Workflow>(wf.id);
            myRequester.DeleteObject<User>("Sebastian");
        }

        /// <summary>
        ///     Test to get a list of all workflows from server.
        /// </summary>
        [TestMethod]
        public void testGetAllWorkflows()
        {
            // generate some TestData
            generateTestUserAndWorkflow("Slubisch");
            // generate some TestData
            generateTestUserAndWorkflow("Romina");
            // generate some TestData
            generateTestUserAndWorkflow("Simon");

            myRequester.InitializeClientProperties("TestAdmin", "");

            IList<Workflow> wFList = myRequester.GetAllElements<Workflow>();

            // Some Loggings
            myListener.WriteLine("Anzahl der workflows: " + wFList.Count());
            // You must close or flush the trace listener to empty the output buffer.
            myListener.Flush();

            Assert.IsTrue(wFList.Count() >= 3);

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

            myRequester.InitializeClientProperties("MaxMustermann", "");

            myRequester.PostObject(newUser);

            string testpwd = "TestPasswort";
            Boolean done = myRequester.checkUser("MaxMustermann", testpwd);
          
            Assert.IsTrue(done);
        }

        /// <summary>
        ///     Test if the right exception is thrown if a non-existent user logs in.
        /// </summary>
        [TestMethod]
        [ExpectedException(typeof(LogInException))]
        public void testCheckNotExistentUser()
        {
            string testpwd = "TestPasswort";
            Boolean done = myRequester.checkUser("Jane", testpwd);
        }
        
        /// <summary>
        ///   Generate some TestData for TestMethods.
        /// </summary>
        /// <param name="username"></param>
        /// <returns></returns>
        private Boolean generateTestUserAndWorkflow(string username)
        {
            Role testRole = new Role();
            testRole.rolename = "Testrole";

            // one User
            User testUser = new User();
            testUser.username = username;
            testUser.roles.Add(testRole);

            // one Workflow
            Workflow newWf = new Workflow();
            newWf.id = "";

            // a startStep
            StartStep startStep = new StartStep();
            startStep.roles.Add("Testrole");
            startStep.id = "";
            newWf.addStep(startStep);

            // an action
            Action act = new Action();
            act.roles.Add("Testrole");
            act.id = "";
            newWf.addStep(act);

            // a final step
            FinalStep fStep = new FinalStep();
            fStep.roles.Add("Testrole");
            fStep.id = "";
            newWf.addStep(fStep);

            myRequester.PostObject(testUser);
            myRequester.PostObject(newWf);

            return true;
        }
    }
}