using Newtonsoft.Json;
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
        public List<Step> Steps { get { return _steps; } set { _steps = value; } }
        private List<Step> _steps; 

        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        public List<Item> Items { get { return _items; } set { _items = value; } }
        private List<Item> _items;

        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        public Form Form { get { return _form; } set { _form = value; } }
        private Form _form;

        /// <summary>
        /// Used for (de)serialization. Do not change the property name.
        /// </summary>
        public bool Active { get { return _active; } set { _active = value; } }
        private bool _active;

        /// <summary>
        /// Constructor for Workflow
        /// </summary>
        public Workflow()
            : base()
        {
            _active = true;
            _steps = new List<Step>();
            _items = new List<Item>();
            _form = new Form();
        }

        /// <summary>
        /// Add new step to workflow. The new step is also added to the "nextSteps" list of the previous step.
        /// </summary>
        /// <param name="step">the step to add</param>
        public void AddStep(Step step)
        {
            _steps.Add(step);
            int stepsCount = Steps.Count;

            if (stepsCount >= 2)
            {
                Steps[stepsCount - 2].NextStepIds.Add(step.Id);
            }
        }

        /// <summary>
        /// Remove last step from workflow and remove link from previous step.
        /// </summary>
        public void RemoveLastStep()
        {
            Step lastStep = _steps[_steps.Count - 1];
            
            if (_steps.Count >= 2)
            {
                _steps[_steps.Count - 2].NextStepIds.Remove(lastStep.Id);
            }

            _steps.RemoveAt(_steps.Count - 1);
        }

        /// <summary>
        /// Clears the workflow.
        /// </summary>
        public void ClearWorkflow()
        {
            _steps.Clear();
        }

        /// <summary>
        /// This method returns a step object by its id.
        /// </summary>
        /// <param name="stepId">the id of requested step</param>
        /// <returns>the requested step</returns>
        public Step GetStepById(String stepId)
        {
            foreach (Step step in _steps)
            {
                if (step.Id.Equals(stepId))
                {
                    return step;
                }
            }
            return null;
        }
    }
}