using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Collections.Specialized;
using System.Collections.ObjectModel;
using System.Windows;
using System.Windows.Input;
using System.Text.RegularExpressions;
using CommunicationLib.Model;
using CommunicationLib;
using RestAPI;
using NLog;
using System.Windows.Threading;

namespace Client.ViewModel
{
    /// <summary>
    /// The WorkflowViewModel contains properties and commands to create a new workflow and to send it to the server.
    /// Furthermore, the properties and commands are used as DataBindings in the graphical user interface.
    /// </summary>
    public class DashboardViewModel : ViewModelBase
    {
        private readonly Logger logger = LogManager.GetCurrentClassLogger();
        
        public DashboardViewModel(MainViewModel mainViewModelInstanz)
        {
            _mainViewModel = mainViewModelInstanz;
            _restRequester = _mainViewModel.restRequester;
        }

        /// <summary>
        /// init update-Methode used while the login
        /// </summary>
        private void updateModel()
        {
            logger.Info("updatedModel()");

            _workflows.Clear();
            IList<Workflow> workflowList = _restRequester.GetAllWorkflowsByUser();
            if (workflowList == null)
            {
                logger.Info("WorkflowList is null");
                workflowList = new List<Workflow>();
            }
            workflowList.ToList().ForEach(_workflows.Add);

            _startableWorkflows.Clear();
            IList<string> startableList = _restRequester.GetStartablesByUser();
            if (startableList == null)
            {
                logger.Info("startableList is null");
                startableList = new List<string>();
            }
            startableList.ToList().ForEach(_startableWorkflows.Add);
            
            foreach (Workflow workflow in _workflows)
            {
                addWorkflowToModel(workflow, startableList);
                _mainViewModel.myComLib.listener.RegisterItemSource(workflow);
            }
            
            logger.Info("Model update finished. Workflows-size: "+_workflows.Count());
            _relevantItems.Clear();
            //_restRequester.GetRelevantItemsByUser(_userName).ToList().ForEach(_relevantItems.Add);
        }
        /// <summary>
        /// update a single workflow
        /// </summary>
        /// <param name="updatedWorkflow">workflow which has to be updated</param>
        /// <param name="startableList">List of startables Workflows</param>
        public void addWorkflowToModel(Workflow updatedWorkflow, IList<string> startableList)
        {
            logger.Info("addWorkflowToModel");
            DashboardWorkflow toUpdate = new DashboardWorkflow(updatedWorkflow);

            if (startableList == null)
            {
                _startableWorkflows.Clear();
                logger.Info("startableList is null");
                startableList = _restRequester.GetStartablesByUser();
                startableList.ToList().ForEach(_startableWorkflows.Add);
            }

            if (_startableWorkflows.Contains(updatedWorkflow.id))
            {
                toUpdate.startPermission = true;
            }
            else
            {
                toUpdate.startPermission = false;
            }
            _relevantItems.Clear();
            _restRequester.GetRelevantItemsByUser(updatedWorkflow.id).ToList().ForEach(_relevantItems.Add);
            Step activeStep;
            DashboardRow row;
            foreach (Item item in _relevantItems)
            {
                activeStep = getStepById(item.getActiveStepId(), updatedWorkflow);
                row = new DashboardRow(item, activeStep, _userName);
                if (activeStep == null)
                {
                    logger.Info("activeStep.label is null");
                }
                else
                {
                    logger.Info("addWorkflowRow activeStep: " + activeStep.label + ", item.id: " + item.id);
                }
                toUpdate.addDashboardRow(row);
            }
            Application.Current.Dispatcher.Invoke(new System.Action(() => _dashboardWorkflows.Add(toUpdate)));
        }

        /// <summary>
        /// updates a single item
        /// </summary>
        /// <param name="item"></param>
        public void updateItem(Item item)
        {
            DashboardRow fittingRow = getWorkflowRowForItem(item);
            Application.Current.Dispatcher.Invoke(new System.Action(() => fittingRow.actItem = item));
        }

        /// <summary>
        /// the method searches the dashboardRow for the given item
        /// </summary>
        /// <param name="item"></param>
        /// <returns>the fitting dashboardrow or null</returns>
        private DashboardRow getWorkflowRowForItem(Item item)
        {
            String workflowId = item.workflowId;
            foreach (DashboardWorkflow workflow in _dashboardWorkflows)
            {
                if (workflowId.Equals(workflow.actWorkflow.id))
                {
                    foreach (DashboardRow dashboardRow in workflow.dashboardRows)
                    {
                        if (dashboardRow.actItem.id.Equals(item.id))
                        {
                            return dashboardRow;
                        }
                    }
                    logger.Info("No similar item was found for " + item.ToString());
                    
                    // create DashboardRow for item
                    Step actStep = getStepById(item.getActiveStepId(), workflow.actWorkflow);
                    DashboardRow newDashbRow = new DashboardRow(item, actStep, userName);
                    workflow.addDashboardRow(newDashbRow);
                    return newDashbRow;
                }
            }
            logger.Info("No parent workflow found for item " + item.ToString());
            return null;
        }

        /// <summary>
        /// finds the step for the given id in a single workflow
        /// </summary>
        /// <param name="id">id of the step</param>
        /// <param name="workflow"></param>
        /// <returns>the fitting step or null</returns>
        private Step getStepById(int id, Workflow workflow)
        {
            foreach (Step step in workflow.steps)
            {
                if (id.ToString().Equals(step.id))
                {
                    return step;
                }
            }
            return null;
        }

        /// <summary>
        /// clears all data in the model
        /// </summary>
        private void deleteModel()
        {
            logger.Info("clear Model()");
            _workflows.Clear();
            _startableWorkflows.Clear();
            _relevantItems.Clear();
            _dashboardWorkflows.Clear();
        }

        /// <summary>
        /// starts a workflow with the given id
        /// </summary>
        /// <param name="id"></param>
        public void createWorkflow(string id)
        {
            logger.Info("createWorkflow()");
            try
            {
                _restRequester.StartWorkflow(id);
            }
            catch (Exception exc)
            {
                logger.Debug(exc.ToString());
                throw;
            }
        }

        /// <summary>
        /// set an item a step forward
        /// </summary>
        /// <param name="stepId"></param>
        /// <param name="itemId"></param>
        public void stepForward(string stepId, string itemId)
        {
            logger.Info("stepForward()");
            _restRequester.StepForward(stepId, itemId);
        }
        /// <summary>
        /// command to step forward, called from the gui
        /// </summary>
        private ICommand _stepForwardCommand;
        public ICommand stepForwardCommand
        {
            get
            {
                if (_stepForwardCommand == null)
                {
                    _stepForwardCommand = new ActionCommand(execute => 
                    {
                        DashboardRow param = (DashboardRow) execute;
                        stepForward(param.actStep.id, param.actItem.id);
                    }, canExecute => true);

                    
                }

                return _stepForwardCommand;
            }
        }
        /// <summary>
        /// logout command for the user
        /// the data is deleted an view changes to to login view
        /// </summary>
        private ICommand _logoutCommand;
        public ICommand logoutCommand
        {
            get
            {
                if (_logoutCommand == null)
                {
                    _logoutCommand = new ActionCommand(excute =>
                        {
                            logger.Info("button clicked");
                            userName = "";
                            deleteModel();
                            _mainViewModel.CurrentPageViewModel = _mainViewModel.loginViewModel;

                            // unregister mainViewModel from CommunicationLib (if logout worked)
                            _mainViewModel.myComLib.Logout();
                        }, canExecute =>
                            {
                                return true;
                            });
                }
                return _logoutCommand;
            }
        }

        private ICommand _startWorkflowCommand;
        public ICommand startWorkflowCommand
        {
            get
            {
                if (_startWorkflowCommand == null)
                {
                    _startWorkflowCommand = new ActionCommand(execute =>
                    {
                        DashboardWorkflow param = (DashboardWorkflow)execute;
                        createWorkflow(param.actWorkflow.id);
                    }, canExecute =>
                    {
                        return true;
                    });
                }
                return _startWorkflowCommand;
            }
        }

        #region properties
        private DashboardRow _selectedRow;
        public DashboardRow selectedRow
        {
            get
            {
                return _selectedRow;
            }
            set
            {
                _selectedRow = value;
                OnChanged("selectedRow");
            }
        }

        private IRestRequester _restRequester;
        private MainViewModel _mainViewModel;

        private String _userName = "";
        public String userName
        {
            get { return _userName; }
            set
            {
                _userName = value;
                logger.Info("Workflows holen mit user " + _userName);

                if (_userName.Equals(""))
                {
                    deleteModel();
                }
                else
                {
                    try
                    {
                        updateModel();
                    }
                    catch (Exception e)
                    {
                        logger.Debug(e.ToString());
                    }
                }
            }
        }

        private ObservableCollection<Workflow> _workflows = new ObservableCollection<Workflow>();
        public ObservableCollection<Workflow> workflows { get { return _workflows; } }

        private ObservableCollection<string> _startableWorkflows = new ObservableCollection<string>();
        public ObservableCollection<string> startableWorkflows { get { return _startableWorkflows; } }

        private ObservableCollection<DashboardWorkflow> _dashboardWorkflows = new ObservableCollection<DashboardWorkflow>();
        public ObservableCollection<DashboardWorkflow> dashboardWorkflows { get { return _dashboardWorkflows; } }

        private List<Item> _relevantItems = new List<Item>();
        #endregion
    }
}
