using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using CommunicationLib.Model;
using RestAPI;

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
            RestRequester.init();

            AbstractWorkflow testWF = new AbstractWorkflow();
            testWF.Id = 1;

            /*Console.WriteLine(testWF.Id);
            AbstractStartStep ass = new AbstractStartStep();
            ass.Name = "Step1";
            ass.Username = "Rahel";
            ass.UserId = 17;
            ass.Id = 0;
            ass.label = "Label";
            testWF.addStep(ass);
            testWF.addStep(new AbstractAction());
            testWF.addStep(new AbstractFinalStep());*/

            String answer = RestRequester.postObject<AbstractWorkflow>(testWF);
            
            Console.WriteLine("POST-Workflow Anfrage erfolgreich geschickt...");
            Console.WriteLine("Antwort: " + answer);
            Console.ReadKey();

            Assert.IsTrue(answer == "True");
        }

        /// <summary>
        /// 
        /// </summary>
        [TestMethod]
        public void testGetWorkflow()
        {
            RestRequester.init();

            int getWFId = 0;
            AbstractWorkflow getWF = RestRequester.getObject<AbstractWorkflow>(getWFId);

            Console.WriteLine("GET-Workflow Anfrage erfolgreich geschickt...");
            Console.WriteLine("Object bekommen: " + getWF);
            Console.WriteLine("Antwort: Workflow Nr. " + getWF.Id + " bekommen.");
            Console.ReadKey();

            Assert.IsTrue(getWFId == getWF.Id);
        }

        /// <summary>
        ///  Test to start a Workflow.
        /// </summary>
        [TestMethod]
        public void testStartWorkflow()
        {
            RestRequester.init();

            int testWfId = 1;
            String testUserName = "Rahel";

            Boolean done = RestRequester.StartWorkflow(testWfId, testUserName);
            
            Console.WriteLine("Start-Workflow Anfrage erfolgreich geschickt...");
            Console.WriteLine("Antwort: Workflow gestartet: " + done);
            Console.ReadKey();

            Assert.IsTrue(done);
        }
    }
}
