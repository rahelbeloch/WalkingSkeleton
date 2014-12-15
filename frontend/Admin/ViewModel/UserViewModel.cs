using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;
using System.Threading.Tasks;
using System.Windows.Input;
using CommunicationLib.Model;
using CommunicationLib.Exception;
using System.Windows;
using CommunicationLib;

namespace Admin.ViewModel
{
    /// <summary>
    /// The UserViewModel contains properties and commands to send new users to the server.
    /// Furthermore, the properties and commands are used as DataBindings to show current users in the graphical user interface. 
    /// </summary>
    public class UserViewModel: ViewModelBase, IDataReceiver
    {
        private MainViewModel _mainViewModel;
        private IRestRequester _restRequester;

        public UserViewModel(MainViewModel mainViewModel)
        {
            _mainViewModel = mainViewModel;
            _restRequester = _mainViewModel.restRequester;
        }

        /// <summary>
        /// Proprety _userCollection to fill list view with users.
        /// </summary>
        private ObservableCollection<User> _userCollection = new ObservableCollection<User>();
        public ObservableCollection<User> userCollection { get { return _userCollection; } }

        /// <summary>
        /// Property for input from username text box.
        /// </summary>
        private string _username = "";
        public string username
        {
            get
            {
                return _username;
            }
            set
            {
                _username = value;
                OnChanged("username");
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
                    _addUserCommand = new ActionCommand(execute =>
                    {
                        try
                        {
                            User newUser = new User();
                            newUser.username = username;

                            _restRequester.PostObject<User>(newUser);

                            // update view model
                            _userCollection.Add(newUser);
                            username = "";
                        }
                        catch (BasicException be)
                        {
                            MessageBox.Show(be.Message);
                        }
                    }, canExecute =>
                    {
                        if (username.Length == 0)
                        {
                            return false;
                        }

                        return true;
                    });
                }

                return _addUserCommand;
            }
        }

        void IDataReceiver.WorkflowUpdate(Workflow workflow)
        {
            Console.WriteLine("Update UserViewModel: WorkflowID = " + workflow.id);
        }

        void IDataReceiver.ItemUpdate(Item item)
        {
            Console.WriteLine("Update UserViewModel: ItemID = " + item.id);
        }

        void IDataReceiver.UserUpdate(User user)
        {
            Console.WriteLine("Update UserViewModel: UserID = " + user.username);
        }
    }
}
