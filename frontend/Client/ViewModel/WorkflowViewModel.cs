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

namespace Client.ViewModel
{
    /// <summary>
    /// The WorkflowViewModel contains properties and commands to create a new workflow and to send it to the server.
    /// Furthermore, the properties and commands are used as DataBindings in the graphical user interface.
    /// </summary>
    public class WorkflowViewModel : ViewModelBase
    {
        public WorkflowViewModel(MainViewModel mainViewModelInstanz)
        {
            _mainViewModel = mainViewModelInstanz;
            _restRequester = _mainViewModel.restRequester;
        }
        private void updateModel()
        {
            Console.WriteLine("updatedModel()");

            _workflows.Clear();
            IList<Workflow> workflowList = _restRequester.GetAllWorkflowsByUser(_userName);
            if (workflowList == null)
            {
                workflowList = new List<Workflow>();
            }
            foreach (Workflow testWorkflow in workflowList)
            {
                Console.WriteLine(testWorkflow);
            }
            workflowList.ToList().ForEach(_workflows.Add);
            OnChanged("workflows");

            _startableWorkflows.Clear();
            IList<int> startableList = _restRequester.GetStartablesByUser(_userName);
            if (startableList == null)
            {
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
            Console.WriteLine("deleteModel()");
            _workflows.Clear();
            _startableWorkflows.Clear();
            _relevantItems.Clear();
        }
        public void createWorkflow(int id, String userName)
        {
            Console.WriteLine("createWorkflow()");
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
            Console.WriteLine("stepForward()");
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
                            Console.WriteLine("button clicked");
                            userName = "";
                            _mainViewModel.CurrentPageViewModel = _mainViewModel.loginViewModel;
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
                Console.WriteLine("Workflows holen mit user " + _userName);

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
