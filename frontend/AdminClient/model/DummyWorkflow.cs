using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AdminClient.model
{
    class DummyWorkflow
    {
        private IList<DummyStep> _steps;
        public IList<DummyStep> steps { get { return _steps; } }

        public DummyWorkflow()
        {
            _steps = new List<DummyStep>();

            // init with default values for testing purposes
            AddStep(new DummyStartStep("Klaus"));
            AddStep(new DummyAction("Müll produzieren", "Klaus"));
            AddStep(new DummyAction("Müll rausbringen", "Michel"));
            AddStep(new DummyFinalStep());
        }

        public void AddStep(DummyStep step)
        {
            _steps.Add(step);
        }

        public void RemoveLastStep()
        {
            _steps.RemoveAt(_steps.Count - 1);
        }
    }
}
