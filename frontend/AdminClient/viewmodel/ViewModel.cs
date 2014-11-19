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

        public ViewModel()
        {
            _dummyWorkflow.CollectionChanged += OnWorkflowChanged;

            // fill model with initial default values
            foreach (var step in _dummyWorkflowModel.steps)
            {
                _dummyWorkflow.Add(step);
            }

            // fill choosable steps with default values
            _choosableSteps.Add(new DummyStartStep());
            _choosableSteps.Add(new DummyAction());
            _choosableSteps.Add(new DummyFinalStep());
        }

        #region properties

        /// <summary>
        /// Property _dummyWorkflow fills list view with dummySteps.
        /// </summary>
        private ObservableCollection<DummyStep> _dummyWorkflow = new ObservableCollection<DummyStep>();
        public ObservableCollection<DummyStep> dummyWorkflow { get { return _dummyWorkflow; } }

        /// <summary>
        /// Property to fill combox box with choosable steps.
        /// </summary>
        private ObservableCollection<DummyStep> _choosableSteps = new ObservableCollection<DummyStep>();
        public ObservableCollection<DummyStep> choosableSteps { get { return _choosableSteps; } }

        /// <summary>
        /// Property to enable textbox for username input.
        /// </summary>
        private bool _enableUserTextBox = false;
        public bool enableUserTextBox 
        { 
            get 
            { 
                return _enableUserTextBox; 
            }
            set
            {
                _enableUserTextBox = value;
                OnChanged("enableUserTextBox");
            }
        }

        /// <summary>
        /// Property to enable textbox for description input.
        /// </summary>
        private bool _enableDescriptionTextBox = false;
        public bool enableDescriptionTextBox
        {
            get
            {
                return _enableDescriptionTextBox;
            }
            set
            {
                _enableDescriptionTextBox = value;
                OnChanged("enableDescriptionTextBox");
            }
        }

        #endregion

        /// <summary>
        /// When the workflow is changed, reconfigure choosable steps for combobox (depending on currently allowed steps).
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        void OnWorkflowChanged(object sender, NotifyCollectionChangedEventArgs e)
        {
            _choosableSteps.Clear();

            if (_dummyWorkflow.Count == 0)
            {
                _choosableSteps.Add(new DummyStartStep());
            }
            else if (_dummyWorkflow[_dummyWorkflow.Count - 1] is DummyStartStep)
            {
                _choosableSteps.Add(new DummyAction());
            }
            else if (_dummyWorkflow.Count >= 2)
            {
                _choosableSteps.Add(new DummyAction());
                _choosableSteps.Add(new DummyFinalStep());
            }
        }

        #region commands

        /// <summary>
        /// Command to open window to add a step to the current workflow.
        /// </summary>
        private ICommand _openAddStepWindow;
        public ICommand openAddStepWindow
        {
            get
            {
                if (_openAddStepWindow == null)
                {
                    _openAddStepWindow = new ActionCommand(func =>
                    {
                        AddStepWindow addElementWindow = new AddStepWindow();
                        addElementWindow.Show();
                    }, func => (_dummyWorkflow.Count == 0) || (_dummyWorkflow.Count > 0 && !(_dummyWorkflow[_dummyWorkflow.Count - 1] is DummyFinalStep)));
                }
                return _openAddStepWindow;
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
                        Console.WriteLine("TODO: send workflow to server");
                        // remove steps from workflow
                        // update model AND viewmodel, because the model is not observable
                        _dummyWorkflowModel.steps.Clear();
                        _dummyWorkflow.Clear();
                    }, func => _dummyWorkflow.Count > 0 && _dummyWorkflow[_dummyWorkflow.Count - 1] is DummyFinalStep);
                }
                return _submitWorkflowCommand;
            }
        }

        #endregion
    }   
}
