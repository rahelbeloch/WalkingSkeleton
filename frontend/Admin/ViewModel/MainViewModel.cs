using CommunicationLib;
using CommunicationLib.Model;
using RestAPI;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;

namespace Admin.ViewModel
{
    /// <summary>
    /// The MainViewModel is a container for all other ViewModels.
    /// It can be used to switch between different ViewModels (and corresponding Views).
    /// </summary>
    public class MainViewModel : ViewModelBase, IDataReceiver
    {
        private WorkflowViewModel _workflowViewModel;
        public WorkflowViewModel workflowViewModel { get { return _workflowViewModel; } }

        private UserViewModel _userViewModel;
        public UserViewModel userViewModel { get { return _userViewModel; }  }

        private IRestRequester _restRequester;
        public IRestRequester restRequester
        {
            get
            {
                if (_restRequester == null)
                {
                    _restRequester = _myComLib.sender;
                }
                return _restRequester;
            }
        }

        private ComLib _myComLib;

        public MainViewModel()
        {
            _myComLib = new ComLib(this);
            // Admin has to register to ComLib! 'TestAdmin' is a Dummy for one admin
            _myComLib.Login("TestAdmin", new SecureString());

            _workflowViewModel = new WorkflowViewModel(this);
            _userViewModel = new UserViewModel(this);

            PageViewModels.Add(workflowViewModel);
            PageViewModels.Add(userViewModel);

            // set starting ViewModel
            CurrentPageViewModel = workflowViewModel;
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

        public void WorkflowUpdate(Workflow workflow)
        {
            Console.WriteLine("neuer Workflow ist angekommen");
            _workflowViewModel.updateWorkflows(workflow);
            
        }

        public void ItemUpdate(Item item)
        {
            throw new NotImplementedException();
        }

        public void UserUpdate(User user)
        {
            userViewModel.UserUpdate(user);
        }

        public void RoleUpdate(Role role)
        {
            userViewModel.RoleUpdate(role);
        }

        #endregion
    }
}
