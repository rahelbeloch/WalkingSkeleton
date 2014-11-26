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

            StartStep ass = new StartStep();
            /*ass.Name = "Step1";
            ass.Username = "Rahel";
            ass.UserId = 17;
            ass.Id = 0;
            ass.label = "Label";*/
            //testWF.addStep(ass);
            testWF.addStep(new Step());
            testWF.addStep(new Step());

            /*Console.WriteLine(testWF.Id);
            
            testWF.addStep(new AbstractAction());
            testWF.addStep(new AbstractFinalStep());*/

            String answer = RestRequester.PostObject<Workflow>(testWF);

            //Console.WriteLine("POST-Workflow Anfrage erfolgreich geschickt...");
            //Console.WriteLine("Antwort: " + answer);
            //Console.ReadKey();

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

            /*
            Console.WriteLine("GET-Workflow Anfrage erfolgreich geschickt...");
            Console.WriteLine("Object bekommen: " + getWF);
            Console.WriteLine("Antwort: Workflow Nr. " + getWF.Id + " bekommen.");
            Console.ReadKey();
            */

            Assert.IsTrue(getWFId == getWF.Id);
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
            Boolean done = RestRequester.StartWorkflow(testWfId, testUserName);

            /*
            Console.WriteLine("Start-Workflow Anfrage erfolgreich geschickt...");
            Console.WriteLine("Antwort: Workflow gestartet: " + done);
            Console.ReadKey();
            */

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

            RestRequester.Init();
            Boolean done = RestRequester.StepForward(stepId, itemId, username);

            Assert.IsTrue(done);
        }

    }
}
