using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using UserClient.ViewModel;


namespace UserClient.ViewModel
{
    class MainViewModel
    {
        public AuthenticationViewModel AuthVM { get; set; }

        public MainViewModel()
        {
            AuthVM = new AuthenticationViewModel();
        }
        private void DoLogin(object obj)
        {
            AuthVM.Authenticate();
        }
        /*
        private ICommand _openAddStepWindow;
        public ICommand openAddStepWindow
        {
            get
            {
                if (_openAddStepWindow == null)
                {
                    _openAddStepWindow = new ActionCommand(func =>
                    {
                        AddStepWindow addElementWindow = new AddStepWindow();
                        addElementWindow.Show();
                    }, func => (_dummyWorkflow.Count == 0) || (_dummyWorkflow.Count > 0 && !(_dummyWorkflow[_dummyWorkflow.Count - 1] is DummyFinalStep)));
                }
                return _openAddStepWindow;
            }
        }
         */
    }
}
