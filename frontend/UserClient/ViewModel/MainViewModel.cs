using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace UserClient.ViewModel
{
    class MainViewModel
    {
        public AuthenticationViewModel AuthVM { get; set; }

        public MainViewModel()
        {
            AuthVM = new AuthenticationViewModel();
        }
    }
}
