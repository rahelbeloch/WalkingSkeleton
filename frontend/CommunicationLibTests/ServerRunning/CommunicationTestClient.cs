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
        public static RestRequester myClientRequester;
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
            myRequester.InitializeClientProperties("TestAdmin", "abc123");

            // initialize user client requester
            myClientRequester = new RestRequester("client", "htp://localhost:18887");
            
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
            Boolean send = generateTestUserAndWorkflow("Rahel");
            // set the right authentication settings to user client
            myClientRequester.InitializeClientProperties("Rahel", "password");
            
            // retrieve workflows from the registered user (test the functionality)
            IList<Workflow> eleList = myRequester.GetAllElements<Workflow>();
            int amountWfs = eleList.Count();
            
            Assert.IsTrue(amountWfs > 0);

            // Clean up the stuff
            myRequester.DeleteObject<Workflow>(eleList[0].id);
            myRequester.DeleteObject<User>("Rahel");
        }

        /// <summary>
        ///  Test to start a Workflow.
        /// </summary>
        [TestMethod]
        public void testStartWorkflow()
        {
            // generate some TestData
            generateTestUserAndWorkflow("Melanie");
            // set the right authentication settings to user client
            myClientRequester.InitializeClientProperties("Melanie", "password");

            IList<Workflow> eleList = myClientRequester.GetAllElements<Workflow>();
            Workflow wf = eleList[0];

            // Test the real functionality
            Boolean done = myClientRequester.StartWorkflow(wf.id);

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
            generateTestUserAndWorkflow("Axel");

            // set the right authentication settings to user client
            myClientRequester.InitializeClientProperties("Axel", "password");

            // retrieve all workflows
            IList<Workflow> eleList = myClientRequester.GetAllElements<Workflow>();

            // get the generated workflow
            Workflow wf = eleList[0];
            // start it
            myClientRequester.StartWorkflow(wf.id);

            // Test the real functionality
            Workflow stepWf = myClientRequester.GetObject<Workflow>(wf.id);
            
            myListener.WriteLine("Anzahl der Steps: " + stepWf.steps.Count());
            myListener.WriteLine("Anzahl der Items: " + stepWf.items.Count());
            // You must close or flush the trace listener to empty the output buffer.
            myListener.Flush();
            
            string stepId = stepWf.steps[1].id;
            string itemId = stepWf.items[0].id;

            Boolean done = myClientRequester.StepForward(stepId, itemId);
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

            // set the right authentication settings to user client
            myClientRequester.InitializeClientProperties("Elizabeth", "password");

            IList<Workflow> eleList = myClientRequester.GetAllElements<Workflow>();
            Workflow wf = eleList[0];

            // Testing the funcionality
            Workflow changeWorkflow = myRequester.GetObject<Workflow>(wf.id);
            // change WF Data
            changeWorkflow.active = false;
            // update the WF
            myRequester.UpdateObject(changeWorkflow);
            // retrieve get to updatedWF
            Workflow updatedWorkflow = myRequester.GetObject<Workflow>(wf.id);

            Assert.IsTrue(updatedWorkflow.active == false);

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
            // set the right authentication settings to user client
            myClientRequester.InitializeClientProperties("Sebastian", "password");

            IList<Workflow> eleList = myClientRequester.GetAllElements<Workflow>();

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

            IList<Workflow> wFList = myRequester.GetAllElements<Workflow>();

            // Some Loggings
            myListener.WriteLine("Anzahl der workflows: " + wFList.Count());
            // You must close or flush the trace listener to empty the output buffer.
            myListener.Flush();

            Assert.IsTrue(wFList.Count() >= 3);

            // Cleanup --> missing
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
            myRequester.PostObject(testRole);

            // one User
            User testUser = new User();
            testUser.username = username;
            testUser.password = "password";
            testUser.roles.Add(testRole);

            // one Workflow
            Workflow newWf = new Workflow();
            newWf.id = "";

            Form newF = new Form();
            newWf.form = newF;

            // a startStep
            StartStep startStep = new StartStep();
            startStep.roleIds.Add("Testrole");
            startStep.id = "";
            newWf.AddStep(startStep);

            // an action
            Action act = new Action();
            act.roleIds.Add("Testrole");
            act.id = "";
            newWf.AddStep(act);

            // a final step
            FinalStep fStep = new FinalStep();
            fStep.roleIds.Add("Testrole");
            fStep.id = "";
            newWf.AddStep(fStep);

            myRequester.PostObject(testUser);
            myRequester.PostObject(newWf);
            
            return true;
        }
    }
}