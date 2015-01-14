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

namespace Admin.ViewModel
{
    /// <summary>
    /// The MainViewModel is a container for all other ViewModels.
    /// It can be used to switch between different ViewModels (and corresponding Views).
    /// </summary>
    public class MainViewModel : ViewModelBase, IDataReceiver
    {
        private static Logger logger = LogManager.GetCurrentClassLogger();
        private String _clientID = "admin";
        public String clientID { get { return _clientID; } }

        private OLD_WorkflowViewModel _oldWorkflowViewModel;
        public OLD_WorkflowViewModel oldWorkflowViewModel { get { return _oldWorkflowViewModel; } }

        private WorkflowDiagramViewModel _workflowViewModel;
        public WorkflowDiagramViewModel workflowViewModel { get { return _workflowViewModel; } }

        private UserViewModel _userViewModel;
        public UserViewModel userViewModel { get { return _userViewModel; }  }

        private LoginViewModel _loginViewModel;
        public LoginViewModel loginViewModel { get { return _loginViewModel; } }

        /// <summary>
        /// Property _userCollection to fill list view with users.
        /// </summary>
        private ObservableCollection<User> _userCollection = new ObservableCollection<User>();
        public ObservableCollection<User> userCollection { get { return _userCollection; } }

        /// <summary>
        /// Property _roleCollection to fill list view with users.
        /// </summary>
        private ObservableCollection<Role> _roleCollection = new ObservableCollection<Role>();
        public ObservableCollection<Role> roleCollection { get { return _roleCollection; } }

        private ObservableCollection<Form> _formCollection = new ObservableCollection<Form>();
        public ObservableCollection<Form> formCollection { get { return _formCollection; } }

        /// <summary>
        /// RestRequester is used for rest request.
        /// </summary>
        private IRestRequester _restRequester;
        public IRestRequester restRequester
        {
            get
            {
                if (_restRequester == null)
                {
                    _restRequester = myComLib.sender;
                }
                return _restRequester;
            }
        }

        /// <summary>
        /// ComLib is used for messaging.
        /// </summary>
        private ComLib _myComLib;
        public ComLib myComLib
        {
            get
            {
                if (_myComLib == null)
                {
                    try
                    {
                        _myComLib = new ComLib(this, clientID);
                    }
                    catch (BasicException e)
                    {
                        MessageBox.Show(e.Message);
                        Environment.Exit(0);
                    }
                }

                return _myComLib;
            }
        }

        public MainViewModel()
        {
            _loginViewModel = new LoginViewModel(this);
            _oldWorkflowViewModel = new OLD_WorkflowViewModel(this);
            _workflowViewModel = new WorkflowDiagramViewModel(this);
            _userViewModel = new UserViewModel(this);

            PageViewModels.Add(loginViewModel);
            PageViewModels.Add(oldWorkflowViewModel);
            PageViewModels.Add(workflowViewModel);
            PageViewModels.Add(userViewModel);

            // set starting ViewModel
            CurrentPageViewModel = loginViewModel;
        }

        #region Commands and Properties

        /// <summary>
        /// Property to store the currently logged in admin name.
        /// </summary>
        private String _admin = "";
        public String admin { get { return _admin; } set { _admin = value; } }

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

                MenuVisibility = _currentPageViewModel == _loginViewModel ? Visibility.Collapsed : Visibility.Visible;
            }
        }

        /// <summary>
        /// Command to log out the current admin.
        /// </summary>
        private ICommand _logoutCommand;
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

        /// <summary>
        /// Property to set the visibility of the menu.
        /// </summary>
        private Visibility _menuVisibility = Visibility.Collapsed;
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
            _oldWorkflowViewModel.InitModel();
            _workflowViewModel.InitModel();
            _userViewModel.InitModel();
        }

        /// <summary>
        /// Clear model after logout.
        /// </summary>
        private void ClearModel()
        {
            _oldWorkflowViewModel.ClearModel();
            _workflowViewModel.ClearModel();
            _userViewModel.ClearModel();
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
            myComLib.Logout();
        }

        #endregion

        #region IDataReceiver callbacks

        void IDataReceiver.WorkflowUpdate(Workflow workflow)
        {
            logger.Info("Received Workflow for Update: ID=" + workflow.id);
            Application.Current.Dispatcher.Invoke(new System.Action(() => _oldWorkflowViewModel.updateWorkflows(workflow)));
            Application.Current.Dispatcher.Invoke(new System.Action(() => _workflowViewModel.updateWorkflows(workflow)));
        }

        void IDataReceiver.ItemUpdate(Item item)
        {
            logger.Info("Received Item for Update: ID=" + item.id);
            Application.Current.Dispatcher.Invoke(new System.Action(() => _oldWorkflowViewModel.updateItemFromWorkflow(item)));
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
            // updated forms arrive here
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
            logger.Info("Delete " + sourceType.ToString() + " ID=" + sourceId);
        }

        public void HandleError(BasicException e)
        {
            Application.Current.Dispatcher.Invoke(new System.Action(() => 
            {
                if (e.GetType() == typeof(LogInException))
                {
                    MessageBox.Show(e.Message);
                    logout();
                }
            }));
        }

        #endregion
    }
}
