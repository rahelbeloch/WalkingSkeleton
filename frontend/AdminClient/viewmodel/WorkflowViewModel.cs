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

namespace AdminClient.viewmodel
{
    class WorkflowViewModel : ViewModelBase, IDataReceiver
    {
        private AbstractWorkflow _workflowModel = new AbstractWorkflow();
        private CommunicationManager communicationManager;
        
        public WorkflowViewModel()
        {
            try
            {
                communicationManager = new CommunicationManager(this);
            }
            catch (Exception e)
            {
                MessageBoxResult result = MessageBox.Show("Es konnte keine Verbindung zum Server hergestellt werden.");
            }
            
            _workflow.CollectionChanged += OnWorkflowChanged;

            // fill choosable steps with default values
            _choosableSteps.Add(new AbstractStartStep());
        }

        #region properties

        /// <summary>
        /// Property _dummyWorkflow fills list view with steps.
        /// TODO: change AbstractStep to Step (not possible at the moment)
        /// </summary>
        private ObservableCollection<AbstractStep> _workflow = new ObservableCollection<AbstractStep>();
        public ObservableCollection<AbstractStep> workflow { get { return _workflow; } }

        /// <summary>
        /// Property to fill combox box with choosable steps.
        /// TODO: change AbstractStep to Step (not possible at the moment)
        /// </summary>
        private ObservableCollection<AbstractStep> _choosableSteps = new ObservableCollection<AbstractStep>();
        public ObservableCollection<AbstractStep> choosableSteps { get { return _choosableSteps; } }

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
        /// TODO: change AbstractStep to Step (not possible at the moment)
        /// </summary>
        private AbstractStep _selectedStep = new AbstractStep();
        public AbstractStep selectedStep
        {
            get
            {
                return _selectedStep;
            }
            set
            {
                _selectedStep = value;

                if (_selectedStep is AbstractStartStep)
                {
                    enableUserTextBox = true;
                    enableDescriptionTextBox = false;
                }
                else if (_selectedStep is AbstractFinalStep)
                {
                    enableUserTextBox = false;
                    enableDescriptionTextBox = false;
                }
                else if (_selectedStep is AbstractAction)
                {
                    enableUserTextBox = true;
                    enableDescriptionTextBox = true;
                }
            }
        }

        /// <summary>
        /// Property for input from userId text box.
        /// </summary>
        private string _userName = "";
        public string userName
        {
            get
            {
                return _userName;
            }
            set
            {
                _userName = value;
                OnChanged("userName");
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
                AbstractStartStep startStep = new AbstractStartStep();
                startStep.Id = 0;
                _choosableSteps.Add(startStep);
            }
            else if (_workflow[_workflow.Count - 1] is AbstractStartStep)
            {
                AbstractAction action = new AbstractAction();
                action.Id = 1;
                _choosableSteps.Add(action);
            }
            else if (_workflow.Count >= 2 && !(_workflow[_workflow.Count - 1] is AbstractFinalStep))
            {
                AbstractAction action = new AbstractAction();
                action.Id = 1;

                AbstractFinalStep finalStep = new AbstractFinalStep();
                finalStep.Id = 2;
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
                    _openAddStepWindow = new ActionCommand(func =>
                    {
                        AddStepWindow addElementWindow = new AddStepWindow();
                        addElementWindow.Show();
                    }, func => (_workflow.Count == 0) || (_workflow.Count > 0 && !(_workflow[_workflow.Count - 1] is AbstractFinalStep)));
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
                        _workflowModel.removeLastStep();
                        _workflow.RemoveAt(_workflow.Count - 1);
                    }, func => _workflow.Count > 0);
                    
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
                        RestAPI.RestRequester.postObject<AbstractWorkflow>(_workflowModel);
                        // remove steps from workflow
                        // update model AND viewmodel, because the model is not observable
                        _workflowModel.clearWorkflow();
                        _workflow.Clear();
                    }, func => _workflow.Count > 0 && _workflow[_workflow.Count - 1] is AbstractFinalStep);
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
                    _addStepCommand = new ActionCommand(func =>
                    {
                        // add step to workflow
                        // update model AND viewmodel, because the model is not observable
                        if (_selectedStep is AbstractStartStep)
                        {
                            AbstractStartStep startStep = new AbstractStartStep();
                            startStep.Name = userName;

                            _workflow.Add(startStep);
                            _workflowModel.addStep(startStep);
                        }
                        else if (_selectedStep is AbstractAction)
                        {
                            AbstractAction action = new AbstractAction();
                            action.Name = userName;
                            action.Name = stepDescription;

                            _workflow.Add(action);
                            _workflowModel.addStep(action);
                        }
                        else if (_selectedStep is AbstractFinalStep)
                        {
                            AbstractFinalStep finalStep = new AbstractFinalStep();

                            _workflow.Add(finalStep);
                            _workflowModel.addStep(finalStep);
                        }

                        userName = "";
                        stepDescription = "";
                    }, func =>
                    {
                        if (selectedStep == null)
                        {
                            return false;
                        }
                        else if (selectedStep is AbstractAction && stepDescription.Length > 0)
                        {
                            return true;
                        }
                        else if (selectedStep is AbstractStartStep)
                        {
                            return true;
                        }
                        else if (selectedStep is AbstractFinalStep)
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

        public void WorkflowUpdate(RegistrationWrapper<AbstractWorkflow> wrappedObject)
        {
            throw new NotImplementedException();
        }

        public void ItemUpdate(RegistrationWrapper<AbstractItem> wrappedObject)
        {
            throw new NotImplementedException();
        }

        public void UserUpdate(RegistrationWrapper<AbstractUser> wrappedObject)
        {
            throw new NotImplementedException();
        }
    }   
}
