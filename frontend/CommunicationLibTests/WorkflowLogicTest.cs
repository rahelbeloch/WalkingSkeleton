using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using CommunicationLib.Model;

namespace UnitTestProject1
{
    /// <summary>
    /// Test class for workflow (backing) bean logic.
    /// </summary>
    [TestClass]
    public class WorkflowLogicTest
    {
        /// <summary>
        /// Test if addStep method works correct.
        /// </summary>
        [TestMethod]
        public void addStepTest()
        {
            Workflow wf = new Workflow();

            Step st1 = new Step();
            Step st2 = new Step();
            Step st3 = new Step();

            wf.AddStep(st1);
            wf.AddStep(st2);
            wf.AddStep(st3);

            Assert.IsTrue(wf.steps[2] == st3);
        }

        /// <summary>
        /// Test if removeLastStep method works correct.
        /// </summary>
        [TestMethod]
        public void removeStepTest()
        {
            Workflow wf = new Workflow();

            Step st1 = new Step();
            Step st2 = new Step();
            Step st3 = new Step();

            wf.AddStep(st1);
            wf.AddStep(st2);
            wf.AddStep(st3);

            wf.RemoveLastStep();
            wf.RemoveLastStep();
            wf.RemoveLastStep();

            Assert.IsTrue(wf.steps.Count == 0);
        }
    }
}
