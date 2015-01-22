using CommunicationLib;
using CommunicationLib.Model;
using CommunicationLib.Exception;
using RestAPI;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Security;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Input;
using System.Diagnostics;
using NLog;
using System.Configuration;

namespace Admin.ViewModel
{
    /// <summary>
    /// The MainViewModel is a container for all other ViewModels.
    /// It can be used to switch between different ViewModels (and corresponding Views).
    /// </summary>
    public class MainViewModel : ViewModelBase, IDataReceiver
    {
        private static Logger logger = LogManager.GetCurrentClassLogger();
        /// <summary>
        /// Property for the clientId
        /// </summary>
        public String clientID { get { return _clientID; } }
        private String _clientID = "admin";

        /// <summary>
        /// Property for the workflowViewModel
        /// </summary>
        public WorkflowDiagramViewModel workflowViewModel { get { return _workflowViewModel; } }
        private WorkflowDiagramViewModel _workflowViewModel;

        /// <summary>
        /// Property for the userViewModel
        /// </summary>
        public UserViewModel userViewModel { get { return _userViewModel; }  }
        private UserViewModel _userViewModel;

        /// <summary>
        /// Property for the loginViewModel
        /// </summary>
        public LoginViewModel loginViewModel { get { return _loginViewModel; } }
        private LoginViewModel _loginViewModel;

        /// <summary>
        /// Property for the formViewModel
        /// </summary>
        public FormViewModel formViewModel { get { return _formViewModel; } }
        private FormViewModel _formViewModel;

        /// <summary>
        /// Property _userCollection to fill list view with users.
        /// </summary>
        public ObservableCollection<User> userCollection { get { return _userCollection; } }
        private ObservableCollection<User> _userCollection = new ObservableCollection<User>();

        /// <summary>
        /// Property _roleCollection to fill list view with users.
        /// </summary>
        public ObservableCollection<Role> roleCollection { get { return _roleCollection; } }
        private ObservableCollection<Role> _roleCollection = new ObservableCollection<Role>();

        /// <summary>
        /// Property for the formCollection
        /// </summary>
        public ObservableCollection<Form> formCollection { get { return _formCollection; } }
        private ObservableCollection<Form> _formCollection = new ObservableCollection<Form>();

        /// <summary>
        /// RestRequester is used for rest request.
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
        /// ComLib is used for messaging.
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
        /// Constructor for the MainViewModel
        /// </summary>
        public MainViewModel()
        {
            _loginViewModel = new LoginViewModel(this);
            _workflowViewModel = new WorkflowDiagramViewModel(this);
            _userViewModel = new UserViewModel(this);
            _formViewModel = new FormViewModel(this);

            PageViewModels.Add(loginViewModel);
            PageViewModels.Add(workflowViewModel);
            PageViewModels.Add(userViewModel);
            PageViewModels.Add(formViewModel);

            // set starting ViewModel
            CurrentPageViewModel = loginViewModel;
        }

        #region Commands and Properties

        /// <summary>
        /// Property to store the currently logged in admin name.
        /// </summary>
        public String admin { get { return _admin; } set { _admin = value; } }
        private String _admin = "";

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

                MenuVisibility = _currentPageViewModel == _loginViewModel ? Visibility.Collapsed : Visibility.Visible;
            }
        }
        private ViewModelBase _currentPageViewModel;

        /// <summary>
        /// Command to log out the current admin.
        /// </summary>
        public ICommand LogoutCommand
        {
            get
            {
                if (_logoutCommand == null)
                {
                    _logoutCommand = new ActionCommand(execute =>
                    {
                        logout();
                    }, canExecute => true);
                }
                return _logoutCommand;
            }
        }
        private ICommand _logoutCommand;

        /// <summary>
        /// Property to set the visibility of the menu.
        /// </summary>
        public Visibility MenuVisibility
        {
            get
            {
                return _menuVisibility;
            }
            set
            {
                _menuVisibility = value;
                OnChanged("MenuVisibility");
            }
        }
        private Visibility _menuVisibility = Visibility.Collapsed;

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
        /// Init model after successful login.
        /// </summary>
        public void InitModel()
        {
            _workflowViewModel.InitModel();
            _userViewModel.InitModel();
            _formViewModel.InitModel();
        }

        /// <summary>
        /// Clear model after logout.
        /// </summary>
        private void ClearModel()
        {
            _workflowViewModel.ClearModel();
            _userViewModel.ClearModel();
            _formViewModel.ClearModel();
        }

        /// <summary>
        /// Clear all ViewModels and switch back to LoginView.
        /// Logout from ComLib.
        /// </summary>
        private void logout()
        {
            admin = "";
            ClearModel();
            CurrentPageViewModel = loginViewModel;
            _restRequester = null;
            myComLib.Logout();
            _myComLib = null;
        }

        #endregion

        #region IDataReceiver callbacks

        void IDataReceiver.WorkflowUpdate(Workflow workflow)
        {
            logger.Info("Received Workflow for Update: ID=" + workflow.id);
            Application.Current.Dispatcher.Invoke(new System.Action(() => _workflowViewModel.UpdateWorkflows(workflow)));
        }

        void IDataReceiver.ItemUpdate(Item item)
        {
            logger.Info("Received Item for Update: ID=" + item.id);
            Application.Current.Dispatcher.Invoke(new System.Action(() => _workflowViewModel.UpdateItemFromWorkflow(item)));
        }

        void IDataReceiver.UserUpdate(User user)
        {
            logger.Info("Received User for Update: ID=" + user.username);
            Application.Current.Dispatcher.Invoke(new System.Action(() => userViewModel.UserUpdate(user)));
        }

        void IDataReceiver.RoleUpdate(Role role)
        {
            logger.Info("Received Role for Update: ID=" + role.rolename);
            Application.Current.Dispatcher.Invoke(new System.Action(() => userViewModel.RoleUpdate(role)));
        }

        void IDataReceiver.FormUpdate(Form form)
        {
            logger.Info("Received Form for Update: ID=" + form.id);
            Application.Current.Dispatcher.Invoke(new System.Action(() => _formViewModel.UpdateForm(form)));

        }

        void IDataReceiver.DataDeletion(Type sourceType, string sourceId)
        {
            if(sourceType == typeof(Workflow))
            {
                // No WorkflowDeletion yet needed
                // Application.Current.Dispatcher.Invoke(new System.Action(() => userViewModel.WorkflowDeletion(sourceId)));
            }
            else if (sourceType == typeof(Role))
            {
                Application.Current.Dispatcher.Invoke(new System.Action(() => userViewModel.RoleDeletion(sourceId)));
            }
            else if(sourceType == typeof(Item))
            {
                // No ItemDeletion yet needed
                // Application.Current.Dispatcher.Invoke(new System.Action(() => userViewModel.ItemDeletion(sourceId)));
            }
            else if(sourceType == typeof(User))
            {
                // No UserDeletion yet needed
                // Application.Current.Dispatcher.Invoke(new System.Action(() => userViewModel.UserDeletion(sourceId)));
            }
            else if (sourceType == typeof(Form))
            {
                Application.Current.Dispatcher.Invoke(new System.Action(() => formViewModel.FormDeletion(sourceId)));
            }
            logger.Info("Delete " + sourceType.ToString() + " ID=" + sourceId);
        }

        /// <summary>
        /// HandleError Method for BasicException
        /// </summary>
        /// <param name="e"></param>
        public void HandleError(BasicException e)
        {
            Application.Current.Dispatcher.Invoke(new System.Action(() => 
            {
                if (e is LogInException)
                {
                    /* Note: If this Exception is caught the ComLib has allready unregistered its client
                     * CommunicationManager-> UnregisterClient()
                     * The Connection has been stopped.
                     */
                    admin = "";
                    ClearModel();
                    CurrentPageViewModel = loginViewModel;
                    _restRequester = null;
                    myComLib.Logout();
                    _myComLib = null;
                    MessageBox.Show(e.Message);
                }
            }));
        }

        #endregion
    }
}
