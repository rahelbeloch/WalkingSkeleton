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
        private static Logger logger = LogManager.GetCurrentClassLogger();

        private MainViewModel _mainViewModel;
        private IRestRequester _restRequester;

        private DiagramViewModel diagramViewModel;
        private ToolBoxViewModel _toolBoxViewModel;
        
        private List<SelectableDesignerItemViewModelBase> itemsToRemove;

        /// <summary>
        /// ToolBoxViewModel
        /// </summary>
        public ToolBoxViewModel toolBoxViewModel { get { return _toolBoxViewModel; } }
        
        /// <summary>
        /// WorkflowDiagramModel
        /// </summary>
        /// <param name="mainViewModel"></param>
        public WorkflowDiagramViewModel(MainViewModel mainViewModel)
        {
            diagramViewModel = new DiagramViewModel();
            diagramViewModel.SelectedItemsCollection.CollectionChanged += OnSelectedItemChanged;
            _mainViewModel = mainViewModel;
            DeleteSelectedItemsCommand = new SimpleCommand(ExecuteDeleteSelectedItemsCommand);
        }
      
        /// <summary>
        /// Observable Collection of Users
        /// </summary>
        public ObservableCollection<User> userCollection { get { return _mainViewModel.userCollection; } }

        /// <summary>
        /// Observable Collection of Roles
        /// </summary>
        public ObservableCollection<Role> roleCollection { get { return _mainViewModel.roleCollection; } }

        /// <summary>
        /// Observable Collection of Forms
        /// </summary>
        public ObservableCollection<Form> formCollection { get { return _mainViewModel.formCollection; } }

        
        #region properties

        /// <summary>
        /// Diagramm View Model
        /// </summary>
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

        private ObservableCollection<Workflow> _workflows = new ObservableCollection<Workflow>();
        /// <summary>
        /// Collection of observable workflows
        /// </summary>
        public ObservableCollection<Workflow> workflows 
        { 
            get { return _workflows; }
        }

        /// <summary>
        /// Property for itemlist of an selected (_actWorkflow) workflow in workflow view.
        /// NOTICE: temporare solution for item view. (There has to be another way...)
        /// </summary>
        public ObservableCollection<Item> items { get { return _items; } }
        private ObservableCollection<Item> _items = new ObservableCollection<Item>();

        /// <summary>
        /// Property for actStep of an selected (_actWorkflow) workflow in workflow view.
        /// </summary>
        public DesignerItemViewModelBase actStep
        {
            get
            {
                return _actStep;
            }
            set
            {
                _actStep = value;
                OnChanged("actStep");
            }
        }
        private DesignerItemViewModelBase _actStep;
        
        /// <summary>
        /// Property of showDetais of an selected Workflow
        /// </summary>
        public Visibility showDetails
        {
            get
            {
                return _showDetails;
            }
            set
            {
                _showDetails = value;
                OnChanged("showDetails");
            }
        }
        private Visibility _showDetails;

        /// <summary>
        /// Property of the Step Visibility (if you're allowed to see the details of the actstep of an item) of an item
        /// </summary>
        public Visibility actStepVisibility
        {
            get
            {
                return _actStepVisibility;
            }
            set
            {
                _actStepVisibility = value;
                OnChanged("actStepVisibility");
            }
        }
        private Visibility _actStepVisibility;

        /// <summary>
        /// Property of the description Visibility
        /// </summary>
        public Visibility descriptionVisibility 
        {
            get
            {
                return _descriptionVisibility;
            }
            set
            {
                _descriptionVisibility = value;
                OnChanged("descriptionVisibility");
            }
        }
        private Visibility _descriptionVisibility;
        
        /// <summary>
        /// Property of the role Visibility
        /// </summary>
        public Visibility roleVisibility
        {
            get
            {
                return _roleVisibility;
            }
            set
            {
                _roleVisibility = value;
                OnChanged("roleVisibility");
            }
        }
        private Visibility _roleVisibility;

        /// <summary>
        /// Propert of the Display View
        /// </summary>
        public Visibility displayView
        {
            get
            {
                return _displayView;
            }
            set
            {
                _displayView = value;
                OnChanged("displayView");
            }
        }
        private Visibility _displayView;
        
        /// <summary>
        /// Property of the edit workflow view
        /// </summary>
        public Visibility editView
        {
            get
            {
                return _editView;
            }
            set
            {
                _editView = value;
                OnChanged("editView");
            }
        }
        private Visibility _editView;
        
        /// <summary>
        /// Property to enable textbox for username input.
        /// </summary>
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
        private bool _enableUserTextBox = false;

        private bool _IsDiagramLocked = false;
        public bool IsDiagramLocked 

        { 
            get 
            { 
                return !DiagramViewModel.locked; 
            }
            
           
        }
        /// <summary>
        /// Property, which indicates if a selected workflow is de-/active.
        /// This property is used for the de-/active button label.
        /// </summary>
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
        private String _workflowActivity = "Deaktivieren";

        /// <summary>
        /// Property to enable textbox for description input.
        /// </summary>
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
        private bool _enableDescriptionTextBox = false;

        /// <summary>
        /// Property of the selected Role
        /// </summary>
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
        private Role _selectedRole = new Role();

        /// <summary>
        /// Property of the selected Form
        /// </summary>
        public Form selectedForm
        {
            get
            {
                return _selectedForm;
            }
            set
            {
                _selectedForm = value;
                OnChanged("selectedForm");
            }
        }
        private Form _selectedForm = new Form();

        /// <summary>
        /// Property for selected workflow in workflow overview.
        /// </summary>
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
                    if (_actWorkflow.form != null)
                    {
                        selectedForm = _actWorkflow.form.Clone<Form>();
                    }

                    showDetails = Visibility.Visible;
                    actStepVisibility = Visibility.Collapsed;
                    WorkflowDiagramConverter.WorkflowToDesignerItems(_actWorkflow, DiagramViewModel);
                    DiagramViewModel.locked = true;
                    OnChanged("IsDiagramLocked");
                }
                else
                {
                    selectedForm = null;
                    showDetails = Visibility.Collapsed;
                    DiagramViewModel.Items.Clear();
                    DiagramViewModel.locked = false;
                    OnChanged("IsDiagramLocked");
                }
                
                OnChanged("actWorkflow");
                OnChanged("items");
            }
        }
        private Workflow _actWorkflow = null;

        /// <summary>
        /// Property for input from step description text box.
        /// </summary>
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
        private string _stepDescription = "";

        /// <summary>
        /// Property for the displayed Tab
        /// </summary>
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
        private int _selectedTabId;

        #endregion

        /// <summary>
        /// Inititialize model after login.
        /// </summary>
        public void InitModel()
        {
            _restRequester = _mainViewModel.restRequester;
            _toolBoxViewModel = new ToolBoxViewModel();
            //OrthogonalPathFinder is a pretty bad attempt at finding path points, it just shows you, you can swap this out with relative
            //ease if you wish just create a new IPathFinder class and pass it in right here
            ConnectorViewModel.PathFinder = new OrthogonalPathFinder();
            
            _showDetails = Visibility.Collapsed;
            _editView = Visibility.Collapsed;
            _displayView = Visibility.Visible;
            _actStepVisibility = Visibility.Collapsed;
            
            try
            {
                logger.Info("Initialize");
                _workflows.Clear();
                
                IList<Workflow> workflowList = _restRequester.GetAllElements<Workflow>();
                if (workflowList == null)
                {
                    workflowList = new List<Workflow>();
                }

                // register workflows as ItemSoure
                foreach (Workflow workflow in workflowList)
                {
                    _mainViewModel.myComLib.Listener.RegisterItemSource(workflow);
                }

                workflowList.ToList().ForEach(_workflows.Add);
            }
            catch (BasicException e)
            {
                MessageBox.Show(e.Message);
            }
            OnChanged("workflows");
        }

        /// <summary>
        /// Clear model after logout.
        /// </summary>
        public void ClearModel()
        {
            userCollection.Clear();
            roleCollection.Clear();
            workflows.Clear();
            DiagramViewModel.Items.Clear();
            selectedRole = null;
            actWorkflow = null;
        }

        /// <summary>
        /// When a workflow is updated (for example its activity), update workflow overview and deselect a selected workflow.
        /// </summary>
        /// <param name="newWorkflow"></param>
        public void UpdateWorkflows(Workflow newWorkflow){
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
        /// <param name="item">item to update</param>
        public void UpdateItemFromWorkflow(Item item)
        {
            Workflow workflowToUpdate = null;

            foreach (Workflow w in _workflows)
            {
                if (w.id.Equals(item.workflowId))
                {
                    workflowToUpdate = w;
                    break;
                }
            }
            workflowToUpdate.items.Remove(item);
            workflowToUpdate.items.Add(item);

            if (_actWorkflow != null)
            {
                items.Clear();
                actWorkflow.items.ForEach(_items.Add);

                OnChanged("items");
            }
            
            OnChanged("workflows");
            
        }
        private void OnSelectedItemChanged(object sender, NotifyCollectionChangedEventArgs e)
        {
            //if (editView == Visibility.Visible)
            //{
            if (diagramViewModel.SelectedItemsCollection.Count == 1)
            {
                actStepVisibility = Visibility.Visible;
                actStep = diagramViewModel.SelectedItemsCollection.First();
                if (actStep is FinalStepViewModel)
                {
                    descriptionVisibility = Visibility.Collapsed;
                    roleVisibility = Visibility.Collapsed;
                }
                if (actStep is StartStepViewModel)
                {
                    descriptionVisibility = Visibility.Collapsed;
                    roleVisibility = Visibility.Visible;
                }
                if (actStep is ActionViewModel)
                {
                    descriptionVisibility = Visibility.Visible;
                    roleVisibility = Visibility.Visible;

                }
            }
            if (diagramViewModel.SelectedItemsCollection.Count == 0 || diagramViewModel.SelectedItemsCollection.Count > 1)
            {
                actStepVisibility = Visibility.Collapsed;
                descriptionVisibility = Visibility.Collapsed;
                roleVisibility = Visibility.Collapsed;
            }
            //}
        }

        #region commands

        /// <summary>
        /// Command to delete a selected Item
        /// </summary>
        public SimpleCommand DeleteSelectedItemsCommand { get; private set; }
        private void ExecuteDeleteSelectedItemsCommand(object parameter)
        {
            if (_mainViewModel.CurrentPageViewModel == this)
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
        }

        /// <summary>
        /// Command to edit the current workflow.
        /// </summary>
        public ICommand editWorkflowCommand
        {
            get
            {
                if (_editWorkflowCommand == null)
                {
                    _editWorkflowCommand = new ActionCommand(execute =>
                        {
                            Boolean activeItems = false;

                            foreach (Item i in actWorkflow.items)
                            {
                                if (!i.finished)
                                {
                                    activeItems = true;
                                    break;
                                }
                            }

                            if (activeItems)
                            {
                                if (MessageBox.Show("Der Workflow besitzt noch unfertige Items! Möchten Sie eine Kopie erstellen?", 
                                    "Workflow kopieren", MessageBoxButton.OKCancel, MessageBoxImage.Question)  == MessageBoxResult.Cancel)
                                {
                                    return;
                                } 
                            }

                            DiagramViewModel.locked = false;
                            OnChanged("IsDiagramLocked");
                            displayView = Visibility.Collapsed;
                            editView = Visibility.Visible;
                            showDetails = Visibility.Collapsed;
                            actStepVisibility = Visibility.Collapsed;
                        }, canExecute => _actWorkflow != null);
                }
                return _editWorkflowCommand;
            }
        }
        private ICommand _editWorkflowCommand;

        /// <summary>
        /// Command to create a new workflow
        /// </summary>
        public ICommand newWorkflowCommand
        {
            get
            {
                if (_newWorkflowCommand == null)
                {
                    _newWorkflowCommand = new ActionCommand(execute =>
                    {
                        actWorkflow = null;
                        DiagramViewModel.Items.Clear();
                        displayView = Visibility.Collapsed;
                        editView = Visibility.Visible;
                        showDetails = Visibility.Collapsed;
                    });
                }
                return _newWorkflowCommand;
            }
        }
        private ICommand _newWorkflowCommand;

        /// <summary>
        /// Command to display a view (visible or not)
        /// </summary>
        public ICommand displayViewCommand
        {
            get
            {
                if (_displayViewCommand == null)
                {
                    _displayViewCommand = new ActionCommand(execute =>
                    {
                        editView = Visibility.Collapsed;
                        displayView = Visibility.Visible;
                        showDetails = Visibility.Visible;
                        DiagramViewModel.locked = true;
                        OnChanged("IsDiagramLocked");
                    });
                }
                return _displayViewCommand;
            }
        }
        private ICommand _displayViewCommand;

        /// <summary>
        /// Command to toggle workflow activity.
        /// </summary>
        public ICommand toggleActivity
        {
            get
            {
                if (_toggleActivity == null)
                {
                    _toggleActivity = new ActionCommand(execute =>
                    {
                        PostWorkflow(true);
                        _workflowActivity = "Deaktivieren";
                        OnChanged("workflowActivity");
                    }, canExecute => _actWorkflow != null);
                }
                return _toggleActivity;
            }
        }
        private ICommand _toggleActivity;

        /// <summary>
        /// Command to save a workflow
        /// </summary>
        public ICommand saveWorkflowCommand
        {
            get
            {
                if (_saveWorkflowCommand == null)
                {
                    _saveWorkflowCommand = new ActionCommand(execute =>
                    {
                        PostWorkflow();
                        
                    }, canExecute => DiagramViewModel.Items.Any());
                }
                return _saveWorkflowCommand;
            }
        }
        private ICommand _saveWorkflowCommand;

        private void PostWorkflow(bool toggleActivity=false)
        {
            try
            {
                if (!DiagramViewModel.Items.Any())
                {
                    MessageBox.Show("Sie können keinen leeren Workflow erstellen.");
                    return;
                }

                Workflow newWorkflow = WorkflowDiagramConverter.DiagramItemsToWorkflow(DiagramViewModel.Items.ToList());
                if (selectedForm != null)
                {
                    newWorkflow.form = selectedForm.Clone<Form>();
                }

                if (actWorkflow != null)
                {
                    newWorkflow.id = actWorkflow.id;
                    newWorkflow.active = actWorkflow.active;
                    if (toggleActivity)
                    {
                        newWorkflow.active = !newWorkflow.active;
                    }
                }

                _restRequester.PostObject(newWorkflow);
                displayView = Visibility.Visible;
                editView = Visibility.Collapsed;
                actStepVisibility = Visibility.Collapsed;
                showDetails = Visibility.Visible;
                MessageBox.Show("Der Workflow wurde erfolgreich gespeichert.");
            }
            catch (BasicException be)
            {
                MessageBox.Show(be.Message);
            }
        }

        #endregion
        private bool ItemsToDeleteHasConnector(List<SelectableDesignerItemViewModelBase> itemsToRemove, FullyCreatedConnectorInfo connector)
        {
            return itemsToRemove.Contains(connector.DataItem);
        }
        
    }   
}