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
            RestRequester.Init();

            Workflow testWF = new Workflow();
            testWF.id = 17;
            
            /*StartStep ass = new StartStep();
            ass.Name = "Step1";
            ass.Username = "Rahel";
            ass.UserId = 17;
            ass.Id = 0;
            ass.label = "Label";
            testWF.addStep(ass);
            testWF.addStep(new Action());
            //testWF.addStep(new FinalStep());*/

            String answer = RestRequester.PostObject<Workflow>(testWF);
            
            Assert.IsTrue(answer == "true");
        }

        /// <summary>
        ///     Test to get a workflow.
        /// </summary>
        [TestMethod]
        public void testGetWorkflow()
        {
            int getWFId = 0;

            RestRequester.Init();
            Workflow getWF = RestRequester.GetObject<Workflow>(getWFId);

            Assert.IsTrue(getWFId == getWF.id);
        }

        /// <summary>
        ///  Test to start a Workflow.
        /// </summary>
        [TestMethod]
        public void testStartWorkflow()
        {
            int testWfId = 1;
            String testUserName = "Rahel";

            RestRequester.Init();
            String done = RestRequester.StartWorkflow(testWfId, testUserName);

            Assert.IsTrue(done == "true");
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

            RestRequester.Init();
            String done = RestRequester.StepForward(stepId, itemId, username);

            Assert.IsTrue(done == "true");
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

            RestRequester.Init();

            RestRequester.UpdateObject(testWF);
            Workflow updatedWorkflow = RestRequester.GetObject<Workflow>(17);

            int steps = updatedWorkflow.steps.Count;

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

    }
}
