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
    public partial class Workflow
    {
        /// <summary>
        /// Add new step to workflow. The new step is also added to the "nextSteps" list of the previous step.
        /// </summary>
        /// <param name="step"></param>
        public void addStep(Step step)
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
    }
}
