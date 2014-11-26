using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{
    /// <summary>
    /// Backing bean implementation for Workflow.
    /// </summary>
    public partial class AbstractWorkflow: AbstractElement
    {
        /// <summary>
        /// Add new step to workflow. The new step is also added to the "nextSteps" list of the previous step.
        /// </summary>
        /// <param name="step"></param>
        public void addStep(AbstractStep step)
        {
            if (Step.Count == 0)
            {
                Step.Add(step);
            }
            else if (Step.Count > 0)
            {
                Step[Step.Count - 1].nextSteps.Add(step);
                Step.Add(step);
            }
        }

        /// <summary>
        /// Remove last step from workflow and remove link from previous step.
        /// </summary>
        public void removeLastStep()
        {
            if (Step.Count == 1)
            {
                Step.RemoveAt(0);
            }
            else if (Step.Count > 1)
            {
                AbstractStep lastStep = Step[Step.Count - 1];

                // remove link from previous step
                Step[Step.Count - 2].nextSteps.Remove(lastStep);
                Step.RemoveAt(Step.Count - 1);
            }
        }

        /// <summary>
        /// Clears the workflow.
        /// </summary>
        public void clearWorkflow()
        {
            Step.Clear();
        }
    }
}
