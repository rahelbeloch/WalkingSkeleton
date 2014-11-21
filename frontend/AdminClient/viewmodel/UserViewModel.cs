using AdminClient.model;
using AdminClient.util;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows.Input;

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
        private string _userId = "";
        public string userId
        {
            get
            {
                return _userId;
            }
            set
            {
                _userId = value;
                OnChanged("userId");
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
                        User newUser = new User();
                        newUser.Id = int.Parse(userId);
                        
                        // update view model
                        _userCollection.Add(newUser);

                        Console.WriteLine("TODO: send user to server");
                        
                        userId = "";
                    }, func =>
                    {
                        if (userId.Length == 0 || !Regex.IsMatch(userId, @"^\d+$"))
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
