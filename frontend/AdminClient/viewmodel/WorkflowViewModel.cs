using AdminClient.util;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Collections.Specialized;
using System.Collections.ObjectModel;
using System.Windows.Input;
using System.Text.RegularExpressions;
using CommunicationLib.Model;
using CommunicationLib;
using System.Windows;
using Action = CommunicationLib.Model.Action;

namespace AdminClient.viewmodel
{
    /// <summary>
    /// The WorkflowViewModel contains properties and commands to create a new workflow and to send it to the server.
    /// Furthermore, the properties and commands are used as DataBindings in the graphical user interface.
    /// </summary>
    class WorkflowViewModel : ViewModelBase
    {
        private Workflow _workflowModel = new Workflow();
        
        public WorkflowViewModel()
        {
            _workflow.CollectionChanged += OnWorkflowChanged;

            // fill choosable steps with default values
            _choosableSteps.Add(new StartStep());
        }

        #region properties

        /// <summary>
        /// Property _dummyWorkflow fills list view with steps.
        /// TODO: change Step to Step (not possible at the moment)
        /// </summary>
        private ObservableCollection<Step> _workflow = new ObservableCollection<Step>();
        public ObservableCollection<Step> workflow { get { return _workflow; } }

        /// <summary>
        /// Property to fill combox box with choosable steps.
        /// TODO: change Step to Step (not possible at the moment)
        /// </summary>
        private ObservableCollection<Step> _choosableSteps = new ObservableCollection<Step>();
        public ObservableCollection<Step> choosableSteps { get { return _choosableSteps; } }

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

        /// <summary>
        /// Property for currently selected step from combo box.
        /// TODO: change Step to Step (not possible at the moment)
        /// </summary>
        private Step _selectedStep = new Step();
        public Step selectedStep
        {
            get
            {
                return _selectedStep;
            }
            set
            {
                _selectedStep = value;

                if (_selectedStep is StartStep)
                {
                    enableUserTextBox = true;
                    enableDescriptionTextBox = false;
                }
                else if (_selectedStep is FinalStep)
                {
                    enableUserTextBox = false;
                    enableDescriptionTextBox = false;
                }
                else if (_selectedStep is Action)
                {
                    enableUserTextBox = true;
                    enableDescriptionTextBox = true;
                }
            }
        }

        /// <summary>
        /// Property for input from username text box.
        /// </summary>
        private string _username = "";
        public string username
        {
            get
            {
                return _username;
            }
            set
            {
                _username = value;
                OnChanged("username");
            }
        }

        /// <summary>
        /// Property for input from step description text box.
        /// </summary>
        private string _stepDescription = "";
        public string stepDescription
        {
            get
            {
                return _stepDescription;
            }
            set
            {
                _stepDescription = value;
                OnChanged("stepDescription");
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

            if (_workflow.Count == 0)
            {
                StartStep startStep = new StartStep();
                _choosableSteps.Add(startStep);
            }
            else if (_workflow[_workflow.Count - 1] is StartStep)
            {
                Action action = new Action();
                _choosableSteps.Add(action);
            }
            else if (_workflow.Count >= 2 && !(_workflow[_workflow.Count - 1] is FinalStep))
            {
                Action action = new Action();

                FinalStep finalStep = new FinalStep();
                _choosableSteps.Add(action);
                _choosableSteps.Add(finalStep);
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
                    _openAddStepWindow = new ActionCommand(execute =>
                    {
                        AddStepWindow addElementWindow = new AddStepWindow();
                        addElementWindow.Show();
                    }, canExecute => (_workflow.Count == 0) || (_workflow.Count > 0 && !(_workflow[_workflow.Count - 1] is FinalStep)));
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
                    _removeLastStepCommand = new ActionCommand(execute =>
                    {
                        // update model AND viewmodel, because the model is not observable
                        _workflowModel.removeLastStep();
                        _workflow.RemoveAt(_workflow.Count - 1);
                    }, canExecute => _workflow.Count > 0);
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
                    _submitWorkflowCommand = new ActionCommand(execute =>
                    {
                        RestAPI.InternalRequester.PostObject<Workflow>(_workflowModel);

                        // remove steps from workflow
                        // update model AND viewmodel, because the model is not observable
                        _workflowModel.clearWorkflow();
                        _workflow.Clear();
                    }, canExecute => _workflow.Count > 0 && _workflow[_workflow.Count - 1] is FinalStep);
                }
                return _submitWorkflowCommand;
            }
        }

        /// <summary>
        /// Command to add a selected step to current workflow.
        /// </summary>
        private ICommand _addStepCommand;
        public ICommand addStepCommand
        {
            get
            {
                if (_addStepCommand == null)
                {
                    _addStepCommand = new ActionCommand(execute =>
                    {
                        // add step to workflow
                        // update model AND viewmodel, because the model is not observable
                        if (_selectedStep is StartStep)
                        {
                            StartStep startStep = new StartStep();
                            startStep.username = username;

                            _workflow.Add(startStep);
                            _workflowModel.addStep(startStep);
                        }
                        else if (_selectedStep is Action)
                        {
                            Action action = new Action();
                            action.username = username;
                            action.description = stepDescription;

                            _workflow.Add(action);
                            _workflowModel.addStep(action);
                        }
                        else if (_selectedStep is FinalStep)
                        {
                            FinalStep finalStep = new FinalStep();

                            _workflow.Add(finalStep);
                            _workflowModel.addStep(finalStep);
                        }

                        // reset inputs
                        username = "";
                        stepDescription = "";
                    }, canExecute =>
                    {
                        if (selectedStep == null)
                        {
                            return false;
                        }
                        else if (selectedStep is Action && username.Length > 0 && stepDescription.Length > 0)
                        {
                            return true;
                        }
                        else if (selectedStep is StartStep && username.Length > 0)
                        {
                            return true;
                        }
                        else if (selectedStep is FinalStep)
                        {
                            return true;
                        }

                        return false;
                    });
                }

                return _addStepCommand;
            }
        }

        #endregion
    }   
}
