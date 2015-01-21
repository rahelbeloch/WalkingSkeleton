using CommunicationLib;
using RestAPI;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;
using NLog;
using CommunicationLib.Model;
using System.Windows;
using CommunicationLib.Exception;
using System.Diagnostics;
using System.Configuration;

namespace Client.ViewModel
{
    /// <summary>
    /// The MainViewModel is a container for all other ViewModels.
    /// It can be used to switch between different ViewModels (and corresponding Views).
    /// </summary>
    public class MainViewModel : ViewModelBase, IDataReceiver
    {
        private String _clientID = "user";
        public String clientID { get { return _clientID; } }

        private readonly Logger logger = LogManager.GetCurrentClassLogger();
        private DashboardViewModel _dashboardViewModel;
        public DashboardViewModel dashboardViewModel { get { return _dashboardViewModel; } }

        private LoginViewModel _loginViewModel;
        public LoginViewModel loginViewModel { get { return _loginViewModel; } }

        // the communication library
        private ComLib _myComLib;
        public ComLib myComLib
        {
            get
            {
                if (_myComLib == null)
                {
                    try
                    {
                        _myComLib = new ComLib(this, clientID, ConfigurationManager.AppSettings[Constants.SERVER_ADDRESS_NAME], ConfigurationManager.AppSettings[Constants.BROKER_ADDRESS_NAME]);
                }
                    catch (BasicException e)
                    {
                        MessageBox.Show(e.Message);
                    }
                }
                return _myComLib;
            }
        }

        private IRestRequester _restRequester;
        public IRestRequester restRequester
        {
            get 
            {
                if (_restRequester == null)
                {
                    _restRequester = myComLib.Sender;
                }
                return _restRequester; 
            }
        }

        private String _userName = "";
        public String username
        {
            get { return _userName; }
            set
            {
                _userName = value;
                _dashboardViewModel.userName = value;
            }
        }

        public MainViewModel()
        {
            _loginViewModel = new LoginViewModel(this);
            _dashboardViewModel = new DashboardViewModel(this);
            PageViewModels.Add(dashboardViewModel);
            PageViewModels.Add(loginViewModel);

            // set starting ViewModel
            CurrentPageViewModel = loginViewModel;
        }

        #region Commands and Properties

        /// <summary>
        /// Command to change the current View/ViewModel.
        /// </summary>
        private ICommand _changePageCommand;
        public ICommand ChangePageCommand
        {
            get
            {
                if (_changePageCommand == null)
                {
                    _changePageCommand = new ActionCommand(
                        p => ChangeViewModel((ViewModelBase)p),
                        p => p is ViewModelBase);
                }
                return _changePageCommand;
            }
        }

        /// <summary>
        /// Property to hold a list of all known ViewModels.
        /// </summary>
        private List<ViewModelBase> _pageViewModels;
        public List<ViewModelBase> PageViewModels
        {
            get
            {
                if (_pageViewModels == null)
                {
                    _pageViewModels = new List<ViewModelBase>();
                }
                    
                return _pageViewModels;
            }
        }

        /// <summary>
        /// Property for the currently shown View/ViewModel.
        /// </summary>
        private ViewModelBase _currentPageViewModel;
        public ViewModelBase CurrentPageViewModel
        {
            get
            {
                return _currentPageViewModel;
            }
            set
            {
                if (_currentPageViewModel != value)
                {
                    _currentPageViewModel = value;
                    OnChanged("CurrentPageViewModel");
                }
            }
        }

        #endregion

        #region Methods

        /// <summary>
        /// This method actually changes the current View/ViewModel.
        /// </summary>
        /// <param name="viewModel">The new current ViewModel.</param>
        private void ChangeViewModel(ViewModelBase viewModel)
        {
            if (!PageViewModels.Contains(viewModel))
            {
                PageViewModels.Add(viewModel);
            }
                
            CurrentPageViewModel = PageViewModels.FirstOrDefault(vm => vm == viewModel);
        }

        /// <summary>
        /// Calls the update methode for a workflow in the dashboardViewModel.
        /// </summary>
        /// <param name="workflow">instance of the new workflow</param>
        public void WorkflowUpdate(Workflow workflow) 
        {
            logger.Info("Received Workflow for Update: ID=" + workflow.Id);
            // route update-handling to subcomponents
            _dashboardViewModel.AddWorkflowToModel(workflow);
        }
        /// <summary>
        /// Calls the update methode for items in the dashboardModel
        /// </summary>
        /// <param name="item">instance of the new item</param>
        public void ItemUpdate(Item item)
        {
            logger.Info("Received Item for Update: ID=" + item.Id);
            Application.Current.Dispatcher.Invoke(new System.Action(() => _dashboardViewModel.UpdateItem(item)));
        }

        public void UserUpdate(User user)
        {
            // update handling
        }

        public void RoleUpdate(Role role)
        {
            // update handling
        }

        public void FormUpdate(Form updatedForm)
        {
            // update handling
        }

        public void DataDeletion(Type sourceType, string sourceId)
        {
            logger.Info("Delete " + sourceType.ToString() + " ID=" + sourceId);
            if (sourceType == typeof(Item))
            {
                Application.Current.Dispatcher.Invoke(new System.Action(() => _dashboardViewModel.DeleteItem(sourceId)));
            }
        }

        public void HandleError(BasicException e)
        {
            // Error handling here
        }

        #endregion
    }
}