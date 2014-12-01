using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{
    /// <summary>
    /// This class represents a Workflow and is a manifestation of a RootElement
    /// </summary>
    public class Workflow : RootElement
    {
        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        private List<Step> _steps;
        public List<Step> steps { get { return _steps; } set { _steps = value; } }

        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        private List<Item> _items;
        public List<Item> items { get { return _items; } set { _items = value; } }

        /// <summary>
        /// Constructor for Workflow
        /// </summary>
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
            steps.Add(step);
        }

        /// <summary>
        /// Remove last step from workflow and remove link from previous step.
        /// </summary>
        public void removeLastStep()
        {
            steps.RemoveAt(steps.Count - 1);
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
