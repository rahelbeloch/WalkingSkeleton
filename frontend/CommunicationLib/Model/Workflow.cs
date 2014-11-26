using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{
    public class Workflow : RootElement
    {
        /// <summary>
        /// Used for (de)serialization!
        /// </summary>
        private List<Step> _steps;
        public List<Step> steps { get { return _steps; } set { _steps = value; } }

        /// <summary>
        /// Used for (de)serialization!
        /// </summary>
        private List<Item> _items;
        public List<Item> items { get { return _items; } set { _items = value; } }

        public Workflow()
            : base()
        {
            _steps = new List<Step>();
            _items = new List<Item>();
        }

        /// <summary>
        /// Add new step to workflow. The new step is also added to the "nextSteps" list of the previous step.
        /// </summary>
        /// <param name="step"></param>
        public void addStep(Step step)
        {
            if (steps.Count == 0)
            {
                steps.Add(step);
            }
            else if (steps.Count > 0)
            {
                steps[steps.Count - 1].nextSteps.Add(step);
                steps.Add(step);
            }
        }

        /// <summary>
        /// Remove last step from workflow and remove link from previous step.
        /// </summary>
        public void removeLastStep()
        {
            if (steps.Count == 1)
            {
                steps.RemoveAt(0);
            }
            else if (steps.Count > 1)
            {
                Step lastStep = steps[steps.Count - 1];

                // remove link from previous step
                steps[steps.Count - 2].nextSteps.Remove(lastStep);
                steps.RemoveAt(steps.Count - 1);
            }
        }

        /// <summary>
        /// Clears the workflow.
        /// </summary>
        public void clearWorkflow()
        {
            steps.Clear();
        }
    }
}
