using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Input;

namespace Admin.ViewModel
{
    public class MainViewModel : ViewModelBase
    {
        public ViewModelBase CurrentView { get; set; }
        
        
        private WorkflowViewModel _workflowViewModel = new WorkflowViewModel();
        public WorkflowViewModel workflowViewModel { get { return _workflowViewModel; } }

        private UserViewModel _userViewModel = new UserViewModel();
        public UserViewModel userViewModel { get { return _userViewModel; }  }
        
        public MainViewModel()
        {
            PageViewModels.Add(userViewModel);
            PageViewModels.Add(workflowViewModel);

            CurrentPageViewModel = PageViewModels[0];
        }


        #region Fields
 
        private ICommand _changePageCommand;
 
        private ViewModelBase _currentPageViewModel;
        private List<ViewModelBase> _pageViewModels;
 
        #endregion
 
 
        #region Properties / Commands
 
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
 
        public List<ViewModelBase> PageViewModels
        {
            get
            {
                if (_pageViewModels == null)
                    _pageViewModels = new List<ViewModelBase>();
 
                return _pageViewModels;
            }
        }
 
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

        private void ChangeViewModel(ViewModelBase viewModel)
        {
            Console.WriteLine("change view model...");
            if (!PageViewModels.Contains(viewModel))
                PageViewModels.Add(viewModel);
 
            CurrentPageViewModel = PageViewModels
                .FirstOrDefault(vm => vm == viewModel);
        }
 
        #endregion
    }
}
