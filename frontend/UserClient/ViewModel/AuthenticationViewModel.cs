using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Windows;
using UserClient.util;

namespace UserClient.ViewModel
{
    public class AuthenticationViewModel : ViewModelBase
    {
        public AuthenticationViewModel()
        {
            CurrentUser = new User { Name = "Name"};
        }

        private bool _IsAuthenticated;
        public bool IsAuthenticated
        {
            get { return _IsAuthenticated; }
            set
            {
                if (value != _IsAuthenticated)
                {
                    _IsAuthenticated = value;
                    OnChanged("IsAuthenticated");
                    OnChanged("IsNotAuthenticated");
                }
            }
        }

        public bool IsNotAuthenticated
        {
            get
            {
                return !IsAuthenticated;
            }
        }

        public bool CanDoAuthenticated(object ignore)
        {
            return IsAuthenticated;
        }

        private User _CurrentUser;
        public User CurrentUser
        {
            get { return _CurrentUser; }
            set

            {
                if (value != _CurrentUser)
                {
                    _CurrentUser = value;
                    OnChanged("CurrentUser");
                }
            }
        }

        public void Authenticate()
        {
            IsAuthenticated = true;
        }

        public void LogOff()
        {
            IsAuthenticated = false;
        }
    }
}
