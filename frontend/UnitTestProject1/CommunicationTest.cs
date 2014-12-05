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
        /// <summary>
        ///  Testmethod to test a transmission of a workflow to server.
        /// </summary>
        [TestMethod]
        public void testSentWorkflow()
        {
            Workflow testWF = new Workflow();
            testWF.id = 17;

            StartStep ass = new StartStep();
            ass.username = "Rahel";
            ass.id = 0;
            testWF.addStep(ass);
            testWF.addStep(new Action());
            testWF.addStep(new FinalStep());

            Boolean send = RestRequester.PostObject<Workflow>(testWF);
            
            Assert.IsTrue(send == true);
        }


        /// <summary>
        ///  Test to start a Workflow.
        /// </summary>
        [TestMethod]
        public void testStartWorkflow()
        {
            int testWfId = 1;
            String testUserName = "Rahel";

            
            Boolean done = RestRequester.StartWorkflow(testWfId, testUserName);

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
            Workflow testWF = new Workflow();
            testWF.id = 17;
            Step testStep = new Step();
            testStep.id = 7;
            testWF.addStep(testStep);


            RestRequester.UpdateObject(testWF);
            Workflow updatedWorkflow = RestRequester.GetObject<Workflow>(17);

            int steps = updatedWorkflow.steps.Count;
            System.Diagnostics.Trace.WriteLine("count steps: " + steps);
            System.Diagnostics.Trace.WriteLine("id workflow: " + testWF.id);

            Assert.IsTrue(steps == 1);
            Assert.IsTrue(updatedWorkflow.steps[steps-1].id == 7);
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
        public void testCheckUser()
        {
            string initString = "TestPasswort";
            SecureString testpwd = new SecureString();
            foreach (char ch in initString)
                testpwd.AppendChar(ch);
            RestRequester.checkUser("Rahel", testpwd);

            Assert.IsTrue(true);
        }

    }
}
