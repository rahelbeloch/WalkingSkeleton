using AdminClient.util;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows.Input;
using CommunicationLib.Model;

namespace AdminClient.viewmodel
{
    class UserViewModel: ViewModelBase
    {
        public UserViewModel()
        {
        }

        /// <summary>
        /// Proprety _userCollection to fill list view with users.
        /// </summary>
        private ObservableCollection<AbstractUser> _userCollection = new ObservableCollection<AbstractUser>();
        public ObservableCollection<AbstractUser> userCollection { get { return _userCollection; } }

        /// <summary>
        /// Property for input from userId text box.
        /// </summary>
        private string _userName = "";
        public string userName
        {
            get
            {
                return _userName;
            }
            set
            {
                _userName = value;
                OnChanged("userName");
            }
        }

        /// <summary>
        /// Command to add a new user.
        /// </summary>
        private ICommand _addUserCommand;
        public ICommand addUserCommand
        {
            get
            {
                if (_addUserCommand == null)
                {
                    _addUserCommand = new ActionCommand(func =>
                    {
                        AbstractUser newUser = new AbstractUser();
                        newUser.Name = userName;
                        
                        // update view model
                        _userCollection.Add(newUser);

                        Console.WriteLine("TODO: send user to server");
                        
                        userName = "";
                    }, func =>
                    {
                        if (userName.Length == 0)
                        {
                            return false;
                        }

                        return true;
                    });
                }

                return _addUserCommand;
            }
        }
    }
}
