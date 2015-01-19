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
using System.Diagnostics;
using CommunicationLib.Exception;
using CommunicationLib.Model.DataModel;

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
        /// Init update-Methode used while the login.
        /// </summary>
        private void InitModel()
        {
            logger.Debug("Init Model");
            _workflows.Clear();
            IList<Workflow> workflowList = null;
            try
            {
                workflowList = _restRequester.GetAllElements<Workflow>();
            } catch (BasicException exc){ MessageBox.Show(exc.Message);}
            
            if (workflowList == null)
            {
                workflowList = new List<Workflow>();
            }
            workflowList.ToList().ForEach(_workflows.Add);

            _startableWorkflows.Clear();
            IList<string> startableList = null;
            try
            {
                startableList = _restRequester.GetStartablesByUser();
            }
            catch (BasicException exc) { MessageBox.Show(exc.Message); }
            

            if (startableList == null)
            {
                startableList = new List<string>();
            }
            startableList.ToList().ForEach(_startableWorkflows.Add);
            
            foreach (Workflow workflow in _workflows)
            {
                AddWorkflowToModel(workflow);
                _mainViewModel.myComLib.listener.RegisterItemSource(workflow);
            }
        }

        /// <summary>
        /// Update a single workflow
        /// </summary>
        /// <param name="updatedWorkflow">workflow which has to be updated</param>
        public void AddWorkflowToModel(Workflow updatedWorkflow)
        {
            logger.Debug("addWorkflowtoModel");
            /*
            logger.Debug("show Formular");
            Form formular = updatedWorkflow.form;
            if (formular.description.Equals("")) {
                formular.description = "test";
            }
            logger.Debug(formular.description);
            FormEntry testEntry = new FormEntry();
            testEntry.key = "test";
            testEntry.datatype = "testValue";
            formular.formDef.Add(testEntry);
            foreach (FormEntry entry in formular.formDef)
            {
                logger.Debug(entry.key);
                logger.Debug(entry.datatype);
            }
             * */
            DashboardWorkflow toUpdate = new DashboardWorkflow(updatedWorkflow);

            IList<string> startableList = null;
            _startableWorkflows.Clear();
            try
            {
                startableList = _restRequester.GetStartablesByUser();
            }
            catch (BasicException exc) { MessageBox.Show(exc.Message); }
            startableList.ToList().ForEach(_startableWorkflows.Add);

            toUpdate.startPermission = _startableWorkflows.Contains(updatedWorkflow.id);

            _relevantItems.Clear();
            _restRequester.GetRelevantItemsByUser(updatedWorkflow.id).ToList().ForEach(_relevantItems.Add);
            Step activeStep;
            DashboardRow row;
            foreach (Item item in _relevantItems)
            {
                activeStep = GetStepById(item.getActiveStepId(), updatedWorkflow);
                row = new DashboardRow(item, activeStep, _userName, updatedWorkflow.form);
                toUpdate.AddDashboardRow(row);
            }

            Application.Current.Dispatcher.Invoke(new System.Action(() => _dashboardWorkflows.Add(toUpdate)));
            logger.Debug("Workflow Update ID="+toUpdate.actWorkflow.id + " ItemCount="+toUpdate.dashboardRows.Count); 
        }

        /// <summary>
        /// Updates a single item.
        /// </summary>
        /// <param name="item">the item to update</param>
        public void UpdateItem(Item item)
        {
            String workflowId = item.workflowId;
            _relevantItems.Clear();
            try
            {
                _restRequester.GetRelevantItemsByUser(workflowId).ToList().ForEach(_relevantItems.Add);
            }
            catch (BasicException exc) { MessageBox.Show(exc.Message); }
            
            if(IsItemInList(item.id, _relevantItems)) {
                DashboardRow fittingRow = GetWorkflowRowForItem(item);
                fittingRow.actItem = item;
                OnChanged("selectedRow");
            }
        }

        /// <summary>
        /// deletes the item with the fitting id from the model
        /// </summary>
        /// <param name="id">id of the item</param>
        public void DeleteItem(String id)
        {
            foreach (DashboardWorkflow dashboardWorkflow in _dashboardWorkflows)
            {
                foreach (DashboardRow dashboardRow in dashboardWorkflow.dashboardRows)
                {
                    if (dashboardRow.actItem.id.Equals(id))
                    {
                        dashboardWorkflow.DeleteDashboardRow(dashboardRow);
                        logger.Debug("Item mit der id " + id + " wurde gelöscht.");     
                        return;
                    }
                }
            }
            logger.Debug("Item mit der id "+ id +" konnte nicht gelöscht werden.");     
        }
        /// <summary>
        /// Checks whether the new item is relevant for the user.
        /// </summary>
        /// <param name="id">id of new or updated item</param>
        /// <param name="itemsList">List of the relevant items for the user</param>
        /// <returns>true if item is in itemsList, else false</returns>
        private Boolean IsItemInList(String id, List<Item> itemsList)
        {
            foreach (Item checkItem in itemsList)
            {
                if (id.Equals(checkItem.id))
                {
                    return true;
                }
            }
            return false;
        }

        /// <summary>
        /// The method searches the dashboardRow for the given item.
        /// </summary>
        /// <param name="item">the given item</param>
        /// <returns>the fitting dashboardrow or null</returns>
        private DashboardRow GetWorkflowRowForItem(Item item)
        {
            String workflowId = item.workflowId;
            bool changed = true;

            // find workflowModel form item
            foreach (DashboardWorkflow workflow in _dashboardWorkflows)
            {
                if (workflowId.Equals(workflow.actWorkflow.id))
                {
                    DashboardRow fittingRow = workflow.dashboardRows.FirstOrDefault(dr => dr.actItem.id.Equals(item.id));

                    if(fittingRow == null)
                    {
                        // create DashboardRow for item
                        Step actStep = GetStepById(item.getActiveStepId(), workflow.actWorkflow);
                        fittingRow = new DashboardRow(item, actStep, userName, workflow.actWorkflow.form);
                        workflow.AddDashboardRow(fittingRow);
                        changed = false;
                    }

                    logger.Info("Item ID=" + item.id + (changed ? " changed" : " added"));
                    return fittingRow;
                }
            }
            return null;
        }

        /// <summary>
        /// Finds the step for the given id in a single workflow.
        /// </summary>
        /// <param name="id">id of the step</param>
        /// <param name="workflow">the associated workflow</param>
        /// <returns>the fitting step or null</returns>
        private Step GetStepById(int id, Workflow workflow)
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
        /// ´Clears all data in the model.
        /// </summary>
        private void DeleteModel()
        {
            logger.Info("Clear Model");
            _workflows.Clear();
            _startableWorkflows.Clear();
            _relevantItems.Clear();
            _dashboardWorkflows.Clear();
        }

        /// <summary>
        /// Starts a workflow with the given id.
        /// </summary>
        /// <param name="id"></param>
        public void CreateWorkflow(string id)
        {
            try
            {
                _restRequester.StartWorkflow(id);
            }
            catch (Exception)
            {
                throw;
            }
        }

        /// <summary>
        /// Set an item a step forward.
        /// </summary>
        /// <param name="stepId">the stepId of the actual step</param>
        /// <param name="itemId">the itemId of the item to forward</param>
        public void StepForward(string stepId, string itemId)
        {
            try
            {
                _restRequester.StepForward(stepId, itemId);
            }
            catch (BasicException exc)
            {
                MessageBox.Show(exc.Message);
            }
            
        }

        /// <summary>
        /// Command to step forward, called from the gui.
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
                        StepForward(param.actStep.id, param.actItem.id);
                }, canExecute => true);
                }
                return _stepForwardCommand;
            }
        }
        private ICommand _saveItemCommand;
        public ICommand saveItemCommand
        {
            get
            {
                if (_saveItemCommand == null)
                {
                    _saveItemCommand = new ActionCommand(execute =>
                    {
                        DashboardRow param = (DashboardRow)execute;
                        try
                        {
                            _restRequester.PostObject(param.actItem);
                        }
                        catch (BasicException exc)
                        {
                            MessageBox.Show(exc.Message);
                        }
                    }, canExecute => true);
                }
                return _saveItemCommand;
            }
        }

        /// <summary>
        /// Logout command for the user;
        /// the data is deleted an view changes to to login view.
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
                            userName = "";
                            DeleteModel();
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
                        CreateWorkflow(param.actWorkflow.id);
                    }, canExecute =>
                    {
                        return true;
                    });
                }
                return _startWorkflowCommand;
            }
        }


        #region properties

        private DashboardRow _selectedRow = null;
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

                if (_userName.Equals(""))
                {
                    DeleteModel();
                }
                else
                {

                        InitModel();
                    
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