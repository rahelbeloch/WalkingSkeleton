using AdminClient.util;
using AdminClient.model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Collections.Specialized;
using System.Collections.ObjectModel;
using System.Windows.Input;

namespace AdminClient.viewmodel
{
    class ViewModel : ViewModelBase
    {
        private DummyWorkflow _dummyWorkflowModel = new DummyWorkflow();

        /// <summary>
        /// Property dummyWorkflow fills list view with dummySteps.
        /// </summary>
        private ObservableCollection<DummyStep> _dummyWorkflow = new ObservableCollection<DummyStep>();
        public ObservableCollection<DummyStep> dummyWorkflow { get { return _dummyWorkflow; } }

        public ViewModel()
        {
            // fill model with initial default values
            foreach (var step in _dummyWorkflowModel.steps)
            {
                _dummyWorkflow.Add(step);
            }
        }

        /// <summary>
        /// Command to delete last step from workflow.
        /// </summary>
        private ICommand _removeLastStepCommand;
        public ICommand removeLastStepCommand
        {
            get
            {
                if (_removeLastStepCommand == null)
                {
                    _removeLastStepCommand = new ActionCommand(func =>
                    {
                        // update model AND viewmodel, because the model is not observable
                        _dummyWorkflowModel.RemoveLastStep();
                        _dummyWorkflow.RemoveAt(_dummyWorkflow.Count - 1);
                    }, func => _dummyWorkflow.Count > 0);
                    
                }
                return _removeLastStepCommand;
            }
        }

        /// <summary>
        /// Command to submit workflow if last step is a final step.
        /// </summary>
        private ICommand _submitWorkflowCommand;
        public ICommand submitWorkflowCommand
        {
            get
            {
                if (_submitWorkflowCommand == null)
                {
                    _submitWorkflowCommand = new ActionCommand(func =>
                    {
                        // update model AND viewmodel, because the model is not observable
                        Console.WriteLine("TODO: send workflow to server");
                    }, func => _dummyWorkflow[_dummyWorkflow.Count - 1] is DummyFinalStep);
                }
                return _submitWorkflowCommand;
            }
        }
    }
}
