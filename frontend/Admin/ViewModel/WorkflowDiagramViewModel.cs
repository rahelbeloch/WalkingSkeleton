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
using Admin.Helpers;

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
        private List<SelectableDesignerItemViewModelBase> itemsToRemove;

        public WorkflowDiagramViewModel(MainViewModel mainViewModel)
        {
            _mainViewModel = mainViewModel;
            _restRequester = _mainViewModel.restRequester;
            DeleteSelectedItemsCommand = new SimpleCommand(ExecuteDeleteSelectedItemsCommand);
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
                if (_actWorkflow != null)
                {
                    _actWorkflow.items.ForEach(_items.Add);
                }
                
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

        #region commands


        public SimpleCommand DeleteSelectedItemsCommand { get; private set; }
        private void ExecuteDeleteSelectedItemsCommand(object parameter)
        {
            itemsToRemove = DiagramViewModel.SelectedItems;
            List<SelectableDesignerItemViewModelBase> connectionsToAlsoRemove = new List<SelectableDesignerItemViewModelBase>();

            foreach (var connector in DiagramViewModel.Items.OfType<ConnectorViewModel>())
            {
                if (ItemsToDeleteHasConnector(itemsToRemove, connector.SourceConnectorInfo))
                {
                    connectionsToAlsoRemove.Add(connector);
                }

                if (ItemsToDeleteHasConnector(itemsToRemove, (FullyCreatedConnectorInfo)connector.SinkConnectorInfo))
                {
                    connectionsToAlsoRemove.Add(connector);
                }

            }
            itemsToRemove.AddRange(connectionsToAlsoRemove);
            foreach (var selectedItem in itemsToRemove)
            {
                if (selectedItem.GetType() == typeof(ConnectorViewModel)) 
                {
                    ConnectorViewModel c = (ConnectorViewModel)selectedItem;
                    c.SourceConnectorInfo.DataItem.enableRightConnector = true;
                    ((FullyCreatedConnectorInfo)c.SinkConnectorInfo).DataItem.enableInputConnector = true;
                }
                DiagramViewModel.RemoveItemCommand.Execute(selectedItem);

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
                            Workflow workflow = WorkflowDiagramConverter.DiagramItemsToWorkflow(DiagramViewModel.Items.ToList());
                            if (workflow != null)
                            {
                                _restRequester.PostObject(workflow);
                            }
                            messageBoxService.ShowInformation(string.Format("Finished saving Diagram "));
                        }
                        catch (BasicException be)
                        {
                            MessageBox.Show(be.Message);
                        }
                    }, canExecute => DiagramViewModel.Items.Any());
                }
                return _saveWorkflowCommand;
            }
        }

        #endregion
        private bool ItemsToDeleteHasConnector(List<SelectableDesignerItemViewModelBase> itemsToRemove, FullyCreatedConnectorInfo connector)
        {
            return itemsToRemove.Contains(connector.DataItem);
        }
    }   
}
