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
                addWorkflowToModel(workflow, startableList);
            }
            logger.Debug("Model update finished. Workflows-size: "+_workflows.Count());
            _relevantItems.Clear();
            //_restRequester.GetRelevantItemsByUser(_userName).ToList().ForEach(_relevantItems.Add);
        }
        public void addWorkflowToModel(Workflow updatedWorkflow, IList<int> startableList)
        {
            logger.Debug("addWorkflowToModel");
            DashboardWorkflow toUpdate = new DashboardWorkflow(updatedWorkflow);
            Application.Current.Dispatcher.Invoke(new System.Action(() => _dashboardWorkflows.Add(toUpdate)));


            if (startableList == null)
            {
                _startableWorkflows.Clear();
                logger.Debug("startableList is null");
                startableList = _restRequester.GetStartablesByUser(_userName);
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
            _restRequester.GetRelevantItemsByUser(updatedWorkflow.id, _userName).ToList().ForEach(_relevantItems.Add);
            Step activeStep;
            DashboardRow row;
            foreach (Item item in _relevantItems)
            {
                activeStep = getStepById(item.getActiveStepId(), updatedWorkflow);
                row = new DashboardRow(item, activeStep, _userName);
                if (activeStep == null)
                {
                    logger.Debug("activeStep.label is null");
                }
                else
                {
                    logger.Debug("addWorkflowRow activeStep: " + activeStep.label + ", item.id: " + item.id);
                }
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
                        stepForward(param.actStep.id, param.actItem.id, param.username);
                    }, canExecute => true);

                    
                }

                return _stepForwardCommand;
            }
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
                        createWorkflow(param.actWorkflow.id, userName);
                    }, canExecute =>
                    {
                        return true;
                    });
                }
                return _startWorkflowCommand;
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
