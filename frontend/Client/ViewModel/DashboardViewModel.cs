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
using RestAPI;
using NLog;

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
        private void updateModel()
        {
            logger.Debug("updatedModel()");

            _workflows.Clear();
            IList<Workflow> workflowList = _restRequester.GetAllWorkflowsByUser(_userName);
            if (workflowList == null)
            {
                logger.Debug("WorkflowList is null");
                workflowList = new List<Workflow>();
            }
            workflowList.ToList().ForEach(_workflows.Add);

            _startableWorkflows.Clear();
            IList<int> startableList = _restRequester.GetStartablesByUser(_userName);
            if (startableList == null)
            {
                logger.Debug("startableList is null");
                startableList = new List<int>();
            }
            startableList.ToList().ForEach(_startableWorkflows.Add);
            foreach (Workflow workflow in _workflows)
            {
                DashboardWorkflow newWorkflow = new DashboardWorkflow(workflow);
                if (_startableWorkflows.Contains(workflow.id))
                {
                    newWorkflow.startPermission = true;
                }
                else
                {
                    newWorkflow.startPermission = false;
                }
                _relevantItems.Clear();
                _restRequester.GetRelevantItemsByUser(workflow.id, _userName).ToList().ForEach(_relevantItems.Add);
                Step activeStep;
                DashboardRow row;
                foreach (Item item in _relevantItems)
                {
                    activeStep = getStepById(item.getActiveStepId(), workflow);
                    row = new DashboardRow(item, activeStep, _userName);
                    newWorkflow.addDashboardRow(row);
                }
                _dashboardWorkflows.Add(newWorkflow);
            }
            _relevantItems.Clear();
            //_restRequester.GetRelevantItemsByUser(_userName).ToList().ForEach(_relevantItems.Add);
        }
        internal void updateWorkflow(Workflow updatedWorkflow)
        {
            Console.WriteLine("Update Workflow mit ID: " + updatedWorkflow.id);
            DashboardWorkflow toUpdate = null;
            foreach (DashboardWorkflow dashboardWorkflow in _dashboardWorkflows)
            {
                if (updatedWorkflow.id == dashboardWorkflow.actWorkflow.id)
                {
                    toUpdate = dashboardWorkflow;
                }
            }
            if (toUpdate == null)
            {
                toUpdate = new DashboardWorkflow(updatedWorkflow);
                _dashboardWorkflows.Add(toUpdate);
                logger.Debug("NO workflow found to update");
            }
            toUpdate = new DashboardWorkflow(updatedWorkflow);

            IList<Workflow> workflowList = _restRequester.GetAllWorkflowsByUser(_userName);
            if (workflowList == null)
            {
                logger.Debug("WorkflowList is null");
                workflowList = new List<Workflow>();
            }
            workflowList.ToList().ForEach(_workflows.Add);

            _startableWorkflows.Clear();
            IList<int> startableList = _restRequester.GetStartablesByUser(_userName);
            if (startableList == null)
            {
                logger.Debug("startableList is null");
                startableList = new List<int>();
            }
            startableList.ToList().ForEach(_startableWorkflows.Add);
            if (_startableWorkflows.Contains(updatedWorkflow.id))
            {
                toUpdate.startPermission = true;
            }
            else
            {
                toUpdate.startPermission = false;
            }
            _relevantItems.Clear();
            _restRequester.GetRelevantItemsByUser(updatedWorkflow.id, _userName).ToList().ForEach(_relevantItems.Add);
            Step activeStep;
            DashboardRow row;
            foreach (Item item in _relevantItems)
            {
                activeStep = getStepById(item.getActiveStepId(), updatedWorkflow);
                row = new DashboardRow(item, activeStep, _userName);
                toUpdate.addDashboardRow(row);
            }
        }
        private Step getStepById(int id, Workflow workflow)
        {
            foreach (Step step in workflow.steps)
            {
                if (id == step.id)
                {
                    return step;
                }
            }
            return null;
        }
        private void deleteModel()
        {
            logger.Debug("clear Model()");
            _workflows.Clear();
            _startableWorkflows.Clear();
            _relevantItems.Clear();
            _dashboardWorkflows.Clear();
        }
        public void createWorkflow(int id, String userName)
        {
            logger.Debug("createWorkflow()");
            try
            {
                _restRequester.StartWorkflow(id, userName);
            }
            catch (Exception exc)
            {
                Console.WriteLine(exc.ToString());
                throw;
            }
        }
        public void stepForward(int stepId, int itemId, String userName)
        {
            logger.Debug("stepForward()");
            _restRequester.StepForward(stepId, itemId, userName);
        }
        private ICommand _logoutCommand;
        public ICommand logoutCommand
        {
            get
            {
                if (_logoutCommand == null)
                {
                    _logoutCommand = new ActionCommand(excute =>
                        {
                            logger.Debug("button clicked");
                            userName = "";
                            deleteModel();
                            _mainViewModel.CurrentPageViewModel = _mainViewModel.loginViewModel;

                            // unregister mainViewModel from CommunicationLib (if logout worked)
                            _mainViewModel.comManager.unregisterClient();
                        }, canExecute =>
                            {
                                return true;
                            });
                }
                return _logoutCommand;
            }
        }

        #region properties
        private IRestRequester _restRequester;
        private MainViewModel _mainViewModel;

        private String _userName = "";
        public String userName
        {
            get { return _userName; }
            set
            {
                _userName = value;
                logger.Debug("Workflows holen mit user " + _userName);

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
                        Console.WriteLine(e.ToString());
                    }
                }
            }
        }
        private ObservableCollection<Workflow> _workflows = new ObservableCollection<Workflow>();
        public ObservableCollection<Workflow> workflows { get { return _workflows; } }

        private ObservableCollection<int> _startableWorkflows = new ObservableCollection<int>();
        public ObservableCollection<int> startableWorkflows { get { return _startableWorkflows; } }

        private ObservableCollection<DashboardWorkflow> _dashboardWorkflows = new ObservableCollection<DashboardWorkflow>();
        public ObservableCollection<DashboardWorkflow> dashboardWorkflows { get { return _dashboardWorkflows; } }

        private List<Item> _relevantItems = new List<Item>();
        #endregion
    }
}
