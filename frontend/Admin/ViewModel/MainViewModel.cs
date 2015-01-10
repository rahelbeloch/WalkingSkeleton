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

namespace Admin.ViewModel
{
    /// <summary>
    /// The MainViewModel is a container for all other ViewModels.
    /// It can be used to switch between different ViewModels (and corresponding Views).
    /// </summary>
    public class MainViewModel : ViewModelBase, IDataReceiver
    {
        private String _clientID = "admin";
        public String clientID { get { return _clientID; } }

        private WorkflowViewModel _workflowViewModel;
        public WorkflowViewModel workflowViewModel { get { return _workflowViewModel; } }

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
                Console.WriteLine("RETURN MY_COM_LIB");
                return _myComLib;
            }
        }

        public MainViewModel()
        {
            //_myComLib = new ComLib(this, clientID);
            // Admin has to register to ComLib! 'TestAdmin' is a Dummy for one admin
            //_myComLib.Login("TestAdmin", "abc123");

            _loginViewModel = new LoginViewModel(this);
            _workflowViewModel = new WorkflowViewModel(this);
            _userViewModel = new UserViewModel(this);

            PageViewModels.Add(loginViewModel);
            PageViewModels.Add(workflowViewModel);
            PageViewModels.Add(userViewModel);

            // set starting ViewModel
            CurrentPageViewModel = loginViewModel;
        }



        #region Commands and Properties

        private String _admin = "";
        public String admin
        {
            get { return _admin; }
            set
            {
                _admin = value;
                //_dashboardViewModel.admin = value;
                //logger.Debug("username gesetzt.");
            }
        }

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

        void IDataReceiver.WorkflowUpdate(Workflow workflow)
        {
            Debug.WriteLine("neuer Workflow ist angekommen");
            _workflowViewModel.updateWorkflows(workflow);
        }

        void IDataReceiver.ItemUpdate(Item item)
        {
            // updated items arrive here
        }

        void IDataReceiver.UserUpdate(User user)
        {
            Application.Current.Dispatcher.Invoke(new System.Action(() => userViewModel.UserUpdate(user)));
        }

        void IDataReceiver.RoleUpdate(Role role)
        {
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
        }

        #endregion
    }
}
