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
using CommunicationLib.Exception;
using System.Diagnostics;
using NLog;
using DiagramDesigner;
using DiagramDesigner.Helpers;

namespace Admin.ViewModel
{
    /// <summary>
    /// The WorkflowViewModel contains properties and commands to create a new workflow and to send it to the server.
    /// Furthermore, the properties and commands are used as DataBindings in the graphical user interface.
    /// </summary>
    public class WorkflowDiagramViewModel : ViewModelBase
    {
        private Workflow _workflowModel = new Workflow();
        private static Logger logger = LogManager.GetCurrentClassLogger();

        private MainViewModel _mainViewModel;
        private IRestRequester _restRequester;

        private DiagramViewModel diagramViewModel = new DiagramViewModel();
        private ToolBoxViewModel _toolBoxViewModel;
        public ToolBoxViewModel toolBoxViewModel { get { return _toolBoxViewModel; } }
        private IMessageBoxService messageBoxService;


        public WorkflowDiagramViewModel(MainViewModel mainViewModel)
        {
            _mainViewModel = mainViewModel;
            _restRequester = _mainViewModel.restRequester;
            _workflow.CollectionChanged += OnWorkflowChanged;
        }
        public DiagramViewModel DiagramViewModel
        {
            get
            {
                return diagramViewModel;
            }
            set
            {
                if (diagramViewModel != value)
                {
                    diagramViewModel = value;
                    diagramViewModel.NotifyChanged("DiagramViewModel");
                }
            }
        }
        public ObservableCollection<User> userCollection { get { return _mainViewModel.userCollection; } }
        
        public ObservableCollection<Role> roleCollection { get { return _mainViewModel.roleCollection; } }

        #region properties

       

        /// <summary>
        /// Property _dummyWorkflow fills list view with steps.
        /// TODO: change Step to Step (not possible at the moment)
        /// </summary>
        private ObservableCollection<Step> _workflow = new ObservableCollection<Step>();
        public ObservableCollection<Step> workflow { get { return _workflow; } }

        private ObservableCollection<Workflow> _workflows = new ObservableCollection<Workflow>();
        public ObservableCollection<Workflow> workflows 
        { 
            get { return _workflows; }
            //set
            //{
            //    _workflows = value;
            //    OnChanged("workflows");
            //}
        }
        /// <summary>
        /// Property for itemlist of an selected (_actWorkflow) workflow in workflow view.
        /// NOTICE: temporare solution for item view. (There has to be another way...)
        /// </summary>
        private ObservableCollection<Item> _items = new ObservableCollection<Item>();
        public ObservableCollection<Item> items { get { return _items; } } 
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
        /// Property, which indicates if a selected workflow is de-/active.
        /// This property is used for the de-/active button label.
        /// </summary>
        private String _workflowActivity = "Deaktivieren";
        public String workflowActivity
        {
            get
            {
                return _workflowActivity;
            }
            set
            {
                if (actWorkflow != null && !actWorkflow.active)
                {
                    _workflowActivity = "Aktivieren";
                }
                else
                {
                    _workflowActivity = "Deaktivieren";
                }
                OnChanged("workflowActivity");
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

        private Role _selectedRole = new Role();
        public Role selectedRole
        {
            get
            {
                return _selectedRole;
            }
            set
            {
                _selectedRole = value;
            }
        }

        /// <summary>
        /// Property for selected workflow in workflow overview.
        /// </summary>
        private Workflow _actWorkflow = null;
        public Workflow actWorkflow
        {
            get
            {
                return _actWorkflow;
            }
            set
            {
                _actWorkflow = value;
                _items.Clear();
                _actWorkflow.items.ForEach(_items.Add);
                OnChanged("actWorkflow");
                OnChanged("items");
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
        /// <summary>
        /// Property for the displayed Tab
        /// </summary>
        private int _selectedTabId;
        public int selectedTabId
        {
            get
            {
                return _selectedTabId;
            }
            set
            {
                _selectedTabId = value;
                OnChanged("selectedTabId");
            }
        }

        #endregion

        public void InitModel()
        {
            _toolBoxViewModel = new ToolBoxViewModel();
            //OrthogonalPathFinder is a pretty bad attempt at finding path points, it just shows you, you can swap this out with relative
            //ease if you wish just create a new IPathFinder class and pass it in right here
            ConnectorViewModel.PathFinder = new OrthogonalPathFinder();
            messageBoxService = ApplicationServicesProvider.Instance.Provider.MessageBoxService;
            
            try
            {
                logger.Info("Initialize");
                _choosableSteps.Add(new StartStep());

                _workflows.Clear();
                // IList<Workflow> workflowList = _restRequester.GetAllElements<Workflow>(); <-- changed method in REST; is generic now; this is the old line
                IList<Workflow> workflowList = _restRequester.GetAllElements<Workflow>();
                if (workflowList == null)
                {
                    workflowList = new List<Workflow>();
                }

                // register workflows as ItemSoure
                foreach (Workflow workflow in workflowList)
                {
                    _mainViewModel.myComLib.listener.RegisterItemSource(workflow);
                }

                workflowList.ToList().ForEach(_workflows.Add);
            }
            catch (BasicException e)
            {
                MessageBox.Show(e.Message);
            }
            OnChanged("workflows");
        }

        public void ClearModel()
        {
            userCollection.Clear();
            roleCollection.Clear();
            workflow.Clear();
            choosableSteps.Clear();
            selectedStep = null;
            selectedRole = null;
            actWorkflow = null;
        }

        /// <summary>
        /// When a workflow is updated (for example its activity), update workflow overview and deselect a selected workflow.
        /// </summary>
        /// <param name="newWorkflow"></param>
        public void updateWorkflows(Workflow newWorkflow){
            bool changed = false;

            foreach (Workflow w in _workflows)
            {
                if (w.id.Equals(newWorkflow.id))
                {
                    _workflows.Remove(w);
                    changed = true;
                    break;
                }
            }

            _workflows.Add(newWorkflow);
            logger.Info("Workflow ID=" + newWorkflow.id + (changed ? " changed" : " added"));

            _actWorkflow = null;
            _items.Clear();
            OnChanged("actWorkflow");
            OnChanged("workflows");
            OnChanged("items");
        }

        /// <summary>
        /// When an item has to be updated (e. g. forward, finish), update the workflow overview and update the item view
        /// </summary>
        /// <param name="item"></param>
        public void updateItemFromWorkflow(Item item)
        {
            Workflow workflowToUpdate = null;
            

            foreach (Workflow w in _workflows)
            {
                if (w.id.Equals(item.workflowId))
                {
                    workflowToUpdate = w;
                    _workflows.Remove(w);
                    workflowToUpdate.items.Remove(item);
                    break;
                }
            }
            workflowToUpdate.items.Add(item);
            _workflows.Add(workflowToUpdate);

            if (_actWorkflow != null)
            {
                _items.Clear();
                _actWorkflow.items.ForEach(_items.Add);
                OnChanged("items");
            }
            
            OnChanged("workflows");
            
        }
            

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
        /// Command to edit the current workflow.
        /// </summary>
        private ICommand _editWorkflowCommand;
        public ICommand editWorkflowCommand
        {
            get
            {
                if (_editWorkflowCommand == null)
                {
                    _editWorkflowCommand = new ActionCommand(execute =>
                        {
                            selectedTabId = 0;
                            _workflowModel = actWorkflow.Clone<Workflow>();
                            _workflow.Clear();
                            foreach (Step step in _workflowModel.steps)
                            {
                                _workflow.Add(step);
                            }
                        }, canExecute => _actWorkflow != null);
                }
                return _editWorkflowCommand;
            }
        }

        /// <summary>
        /// Command to toggle workflow activity.
        /// </summary>
        private ICommand _toggleActivity;
        public ICommand toggleActivity
        {
            get
            {
                if (_toggleActivity == null)
                {
                    _toggleActivity = new ActionCommand(execute =>
                    {
                        try
                        {

                            if (_actWorkflow.active)
                            {
                                _actWorkflow.active = false;
                                _restRequester.UpdateObject(_actWorkflow);
                            }
                            else
                            {
                                _actWorkflow.active = true;
                                _restRequester.UpdateObject(_actWorkflow);
                            }
                            _workflowActivity = "Deaktivieren";
                            OnChanged("workflowActivity");
                        }
                        catch (BasicException e)
                        {
                            MessageBox.Show(e.Message);
                        }
                    }, canExecute => _actWorkflow != null);
                }
                return _toggleActivity;
            }
        }
        private ICommand _resetWorkflowCommand;
        public ICommand resetWorkflowCommand
        {
            get
            {
                if (_resetWorkflowCommand == null)
                {
                    _resetWorkflowCommand = new ActionCommand(execute =>
                        {
                            _workflowModel.clearWorkflow();
                            workflow.Clear();
                        }, canExecute => true);
                }
                return _resetWorkflowCommand;
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
                        try
                        {
                            // set all ids in workflow to empty string, to avoid sending 'null'; otherwise problems with parsing!
                            if (_workflowModel != actWorkflow)
                            {
                                _workflowModel.id = "";     
                            }

                            foreach (Step step in _workflowModel.steps)
                            {
                                if (step.GetType() == typeof(StartStep) || step.GetType() == typeof(Action))
                                {
                                    step.nextStepIds = new List<string>();
                                }
                                step.id = "";
                            }

                            _restRequester.PostObject(_workflowModel);

                            // remove steps from workflow
                            // update model AND viewmodel, because the model is not observable
                            _workflowModel.clearWorkflow();
                            _workflowModel = new Workflow();
                            _workflow.Clear();
                        }
                        catch (BasicException be)
                        {
                            MessageBox.Show(be.Message);
                        }
                    }, canExecute => _workflow.Count > 0 && _workflow[_workflow.Count - 1] is FinalStep);
                }
                return _submitWorkflowCommand;
            }
        }

        ////neues Command anfang
        private ICommand _saveWorkflowCommand;
        public ICommand saveWorkflowCommand
        {
            get
            {
                if (_saveWorkflowCommand == null)
                {
                    _saveWorkflowCommand = new ActionCommand(execute =>
                    {
                        try
                        {
                            // from demo start
                            if (!DiagramViewModel.Items.Any())
                            {
                                messageBoxService.ShowError("There must be at least one item in order save a diagram");
                                return;
                            }
                            _workflowModel = new Workflow();


                            foreach (var designerItem in DiagramViewModel.Items)
                            {
                                if(designerItem.GetType() == typeof(ConnectorViewModel)) 
                                {
                                    DesignerItemViewModelBase startItem = ((FullyCreatedConnectorInfo)((ConnectorViewModel)designerItem).SourceConnectorInfo).DataItem;
                                    DesignerItemViewModelBase endItem = ((FullyCreatedConnectorInfo)((ConnectorViewModel)designerItem).SinkConnectorInfo).DataItem;

                                    Console.WriteLine("start: "+ startItem +", ende: "+ endItem);
                                }
                                
                                
                            }

                            /*
                            //Save all PersistDesignerItemViewModel
                            foreach (var startStep in DiagramViewModel.Items.OfType<StartStepViewModel>())
                            {

                                StartStep persistStartStep = new StartStep();
                                //(startStep.Id, startStep.Left, startStep.Top, startStep.HostUrl);
                                //_workflowModel.addStep(persistStartStep);
                                logger.Info("StartStepPoint: " + startStep.Left);
                            }
                            //Save all PersistDesignerItemViewModel
                            foreach (var actionStep in DiagramViewModel.Items.OfType<ActionViewModel>())
                            {
                                Action persistAction = new Action();


                                //actionStep.Id, actionStep.Left, actionStep.Top, actionStep.HostUrl);
                                //_workflowModel.addStep(persistAction);


                            }
                            foreach (var connection in DiagramViewModel.Items.OfType<ConnectorViewModel>())
                            {
                                logger.Info("EndPoint: " + connection.EndPoint);
                                logger.Info("SouceA: " + connection.SourceA);
                                logger.Info("SouceB: " + connection.SourceB);

                            }
                            foreach (var finalStep in DiagramViewModel.Items.OfType<FinalStepViewModel>())
                            {
                                FinalStep persistFinalStep = new FinalStep();
                                //actionStep.Id, actionStep.Left, actionStep.Top, actionStep.HostUrl);
                                //_workflowModel.addStep(persistFinalStep);

                            } */



                            messageBoxService.ShowInformation(string.Format("Finished saving Diagram "));

                            // _restRequester.PostObject(_workflowModel);

                            // remove steps from workflow
                            // update model AND viewmodel, because the model is not observable
                            _workflowModel.clearWorkflow();
                            _workflow.Clear();
                        }
                        catch (BasicException be)
                        {
                            MessageBox.Show(be.Message);
                        }
                    }, canExecute => _workflowModel != null);
                }
                return _saveWorkflowCommand;
            }
        }

        ////neues Command ende


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
                            startStep.roleIds.Add(selectedRole.rolename);

                            _workflow.Add(startStep);
                            _workflowModel.addStep(startStep);
                        }
                        else if (_selectedStep is Action)
                        {
                            Action action = new Action();
                            
                            action.description = stepDescription;
                            action.roleIds.Add(selectedRole.rolename);

                            _workflow.Add(action);
                            _workflowModel.addStep(action);
                        }
                        else if (_selectedStep is FinalStep)
                        {
                            FinalStep finalStep = new FinalStep();
                            finalStep.roleIds.Add(selectedRole.rolename);

                            _workflow.Add(finalStep);
                            _workflowModel.addStep(finalStep);
                        }

                        // reset inputs
                        stepDescription = "";
                        
                        
                    }, canExecute =>
                    {
                        if (selectedStep == null)
                        {
                            return false;
                        }
                        else if (selectedStep is Action &&  stepDescription.Length > 0)
                        {
                            return true;
                        }
                        else if (selectedStep is StartStep && selectedRole != null)
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
