using CommunicationLib;
using RestAPI;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;
using NLog;
namespace Client.ViewModel
{
    /// <summary>
    /// The MainViewModel is a container for all other ViewModels.
    /// It can be used to switch between different ViewModels (and corresponding Views).
    /// </summary>
    public class MainViewModel : ViewModelBase
    {
        private readonly Logger logger = LogManager.GetCurrentClassLogger();
        private DashboardViewModel _dashboardViewModel;
        public DashboardViewModel dashboardViewModel { get { return _dashboardViewModel; } }

        private LoginViewModel _loginViewModel;
        public LoginViewModel loginViewModel { get { return _loginViewModel; } }

        private IRestRequester _restRequester;
        public IRestRequester restRequester
        {
            get {
                if (_restRequester == null)
                {
                    _restRequester = new RestRequester();
                }
                return _restRequester; }
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
 
        #endregion
    }
}
