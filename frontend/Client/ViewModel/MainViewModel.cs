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
        /// <summary>
        /// Identifikation string for this client.
        /// </summary>
        public String clientID { get { return _clientID; } }
        private String _clientID = "user";
        
        private readonly Logger logger = LogManager.GetCurrentClassLogger();
        
        /// <summary>
        /// Viewmodel for the users dashboard with workflows.
        /// </summary>
        public DashboardViewModel dashboardViewModel { get { return _dashboardViewModel; } }
        private DashboardViewModel _dashboardViewModel;
        
        /// <summary>
        /// Viewmodel for login.
        /// </summary>
        public LoginViewModel loginViewModel { get { return _loginViewModel; } }
        private LoginViewModel _loginViewModel;
        
        /// <summary>
        /// The communication library.
        /// </summary>
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
        private ComLib _myComLib;
        
        /// <summary>
        /// The requester for rest requests.
        /// </summary>
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
        private IRestRequester _restRequester;
        
        /// <summary>
        /// The username of the logged in user.
        /// </summary>
        public String username
        {
            get { return _userName; }
            set
            {
                _userName = value;
                _dashboardViewModel.userName = value;
            }
        }
        private String _userName = "";
        
        /// <summary>
        /// Default constructor. Initializes the dashboard und login view models and arranges them.
        /// </summary>
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
        private ICommand _changePageCommand;

        /// <summary>
        /// Property to hold a list of all known ViewModels.
        /// </summary>
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
        private List<ViewModelBase> _pageViewModels;

        /// <summary>
        /// Property for the currently shown View/ViewModel.
        /// </summary>
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
        private ViewModelBase _currentPageViewModel;
        
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
            logger.Info("Received Workflow for Update: ID=" + workflow.id);
            // route update-handling to subcomponents
            _dashboardViewModel.AddWorkflowToModel(workflow);
        }
        
        /// <summary>
        /// Calls the update methode for items in the dashboardModel.
        /// </summary>
        /// <param name="item">instance of the new item</param>
        public void ItemUpdate(Item item)
        {
            logger.Info("Received Item for Update: ID=" + item.id);
            Application.Current.Dispatcher.Invoke(new System.Action(() => _dashboardViewModel.UpdateItem(item)));
        }

        /// <summary>
        /// Update method for updates from the server concerning the user.
        /// </summary>
        /// <param name="user">new/edited user</param>
        public void UserUpdate(User user)
        {
            // update handling
        }

        /// <summary>
        /// Update method for updates from the server concerning roles.
        /// </summary>
        /// <param name="role">the new/updated role</param>
        public void RoleUpdate(Role role)
        {
            // update handling
        }

        /// <summary>
        /// Update method for updates from the server concerning forms.
        /// </summary>
        /// <param name="updatedForm">the new/updated form</param>
        public void FormUpdate(Form updatedForm)
        {
            // update handling
        }

        /// <summary>
        /// React to deletions from the server by deleting the concerning view models.
        /// </summary>
        /// <param name="sourceType">type of deleted object</param>
        /// <param name="sourceId">id of deleted object</param>
        public void DataDeletion(Type sourceType, string sourceId)
        {
            logger.Debug("Delete " + sourceType.ToString() + " ID=" + sourceId);
            if (sourceType == typeof(Item))
            {
                Application.Current.Dispatcher.Invoke(new System.Action(() => _dashboardViewModel.DeleteItem(sourceId)));
            }
        }

        /// <summary>
        /// Callback from CommunicationManager in ComLib. 
        /// </summary>
        /// <param name="e">the exception, which happened</param>
        public void HandleError(BasicException e)
        {
            // Error handling here
        }

        #endregion
    }
}