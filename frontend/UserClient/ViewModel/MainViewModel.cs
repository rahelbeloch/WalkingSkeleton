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
            Console.WriteLine("Authentication");
            AuthVM.Authenticate();
        }
        
        private ActionCommand _doLogin;
        
        public ActionCommand doLogin
        {
            get
            {
                if (_doLogin == null)
                {
                    _doLogin = new ActionCommand(func => DoLogin());
                }
                return _doLogin;
            }
        }

        private void DoLogin()
        {
            Console.WriteLine("Authentication");
            AuthVM.Authenticate();
        }
         
    }
}
