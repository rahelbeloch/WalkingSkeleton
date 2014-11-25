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

            wf.addStep(st1);
            wf.addStep(st2);
            wf.addStep(st3);

            Assert.IsTrue(wf.Step[2] == st3);

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

            wf.addStep(st1);
            wf.addStep(st2);
            wf.addStep(st3);

            wf.removeLastStep();
            wf.removeLastStep();
            wf.removeLastStep();

            Assert.IsTrue(wf.Step.Count == 0);

        }

        /// <summary>
        /// Test if nextStep reference is removed correctly.
        /// </summary>
        [TestMethod]
        public void removeStepNextStepTest()
        {
            Workflow wf = new Workflow();

            Step st1 = new Step();
            Step st2 = new Step();
            Step st3 = new Step();

            wf.addStep(st1);
            wf.addStep(st2);
            wf.addStep(st3);

            wf.removeLastStep();

            Assert.IsTrue(wf.Step[1].nextSteps.Count == 0);
        }

        /// <summary>
        /// Test if nextStep reference is correct.
        /// </summary>
        [TestMethod]
        public void addStepNextStepTest()
        {
            Workflow wf = new Workflow();

            Step st1 = new Step();
            Step st2 = new Step();

            wf.addStep(st1);
            wf.addStep(st2);

            Assert.IsTrue(wf.Step[0].nextSteps[0] == st2);
        }
    }
}
