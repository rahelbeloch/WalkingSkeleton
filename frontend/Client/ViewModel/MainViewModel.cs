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
namespace Client.ViewModel
{
    /// <summary>
    /// The MainViewModel is a container for all other ViewModels.
    /// It can be used to switch between different ViewModels (and corresponding Views).
    /// </summary>
    public class MainViewModel : ViewModelBase, IDataReceiver
    {
        private readonly Logger logger = LogManager.GetCurrentClassLogger();
        private DashboardViewModel _dashboardViewModel;
        public DashboardViewModel dashboardViewModel { get { return _dashboardViewModel; } }

        private LoginViewModel _loginViewModel;
        public LoginViewModel loginViewModel { get { return _loginViewModel; } }

        private CommunicationManager _comManager;
        public CommunicationManager comManager
        {
            get
            {
                if (_comManager == null)
                {
                    _comManager = new CommunicationManager();
                }
                return _comManager;
            }
        }

        private IRestRequester _restRequester;
        public IRestRequester restRequester
        {
            get 
            {
                if (_restRequester == null)
                {
                    _restRequester = new RestRequester();
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
                logger.Debug("username gesetzt.");
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


        void IDataReceiver.WorkflowUpdate(Workflow workflow) 
        {
            Console.WriteLine("Received Workflow for Update: ID = " + workflow.id);
            // route update-handling to subcomponents
            _dashboardViewModel.addWorkflowToModel(workflow, null);
        }

        void IDataReceiver.ItemUpdate(Item item)
        {
            logger.Debug("Update Item: " + item.ToString());
            // route update-handling to subcomponents
            // route to itemViewModel etc. (update-method) to react to changes in one of my items
        }

        void IDataReceiver.UserUpdate(User user)
        {
            // route update-handling to subcomponents
            // route to userViewModel etc. (update-method) to react to changes in my settings
        }

        void IDataReceiver.RoleUpdate(Role role)
        {
            // route update-handling to subcomponents
            // route to userViewModel etc. (update-method) to react to changes in my settings
        }

        #endregion
    }
}
