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
    public class UserViewModel: ViewModelBase
    {
        private MainViewModel _mainViewModel;
        private IRestRequester _restRequester;

        public UserViewModel(MainViewModel mainViewModel)
        {
            _mainViewModel = mainViewModel;
            _restRequester = _mainViewModel.restRequester;

            initModel();
        }

        private void initModel()
        {
            // update userlist
            IList<User> allUsers = _restRequester.GetAllElements<User>();
            foreach (User user in allUsers)
            {
                _mainViewModel.userCollection.Add(user);
            }

            // update rolelist
            IList<Role> allRoles =  _restRequester.GetAllElements<Role>();
            foreach(Role role in allRoles)
            {
                roleCheckboxRows.Add(new RoleCheckboxRow(role, false));
                _mainViewModel.roleCollection.Add(role);
            }
        }

        public ObservableCollection<User> userCollection { get { return _mainViewModel.userCollection; } }
        public ObservableCollection<Role> roleCollection { get { return _mainViewModel.roleCollection; } }

        /// <summary>
        /// Property _roleCheckboxRoles to fill list view with selectable roles.
        /// </summary>
        private ObservableCollection<RoleCheckboxRow> _roleCheckboxRows = new ObservableCollection<RoleCheckboxRow>();
        public ObservableCollection<RoleCheckboxRow> roleCheckboxRows { get { return _roleCheckboxRows; } }

        private User _selectedUser;
        public User selectedUser
        {
            get
            {
                return _selectedUser;
            }
            set
            {
                _selectedUser = value;
                if (_selectedUser != null)
                {
                    username = _selectedUser.username;

                    postUserButtonText = "Nutzer ändern";
                    enableUserTextBox = false;

                    foreach (RoleCheckboxRow roleCheckboxRow in _roleCheckboxRows)
                    {
                        roleCheckboxRow.isSelected = _selectedUser.roles.Any(i => i.id == roleCheckboxRow.role.id);
                    }
                }
                else
                {
                    enableUserTextBox = true;
                }
            }
        }

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
        /// Property for input from rolename text box.
        /// </summary>
        private string _rolename = "";
        public string rolename
        {
            get
            {
                return _rolename;
            }
            set
            {
                _rolename = value;
                OnChanged("rolename");
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
                            postUser();
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

        private bool _enableUserTextBox = true;
        public bool enableUserTextBox
        {
            get
            {
                return _enableUserTextBox;
            }
            set
            {
                _enableUserTextBox = value;
                OnChanged("enableUserTextBox");
            }
        }

        private string _postUserButtonText = "Nutzer hinzufügen";
        public string postUserButtonText 
        { 
            get 
            { 
                return _postUserButtonText; 
            } 
            set 
            { 
                _postUserButtonText = value; 
                OnChanged("postUserButtonText"); 
            } 
        }

        private void postUser()
        {
            User newUser = new User();
            newUser.id = username;

            foreach (RoleCheckboxRow actRow in roleCheckboxRows)
            {
                if (actRow.isSelected)
                {
                    newUser.roles.Add(actRow.role);
                }
            }

            _restRequester.PostObject(newUser);

            deselect();
        }

        /// <summary>
        /// Command to add a new role.
        /// </summary>
        private ICommand _addRoleCommand;
        public ICommand addRoleCommand
        {
            get
            {
                if (_addRoleCommand == null)
                {
                    _addRoleCommand = new ActionCommand(execute =>
                    {
                        try
                        {
                            Role newRole = new Role();
                            newRole.rolename = rolename;

                            _restRequester.PostObject(newRole);
                            rolename = "";
                        }
                        catch (BasicException be)
                        {
                            MessageBox.Show(be.Message);
                        }
                    }, canExecute =>
                    {
                        if (rolename.Length == 0)
                        {
                            return false;
                        }

                        return true;
                    });
                }

                return _addRoleCommand;
            }
        }

        private ICommand _deselectCommand;
        public ICommand deselectCommand
        {
            get
            {
                if (_deselectCommand == null)
                {
                    _deselectCommand = new ActionCommand(execute =>
                        {
                            deselect();
                        }, canExecute => _selectedUser != null);
                }

                return _deselectCommand;
            }
        }

        /// <summary>
        /// Deselect 
        /// </summary>
        private void deselect()
        {
            _selectedUser = null;
            username = "";
            postUserButtonText = "Nutzer hinzufügen";
            enableUserTextBox = true;

            foreach (RoleCheckboxRow roleCheckboxRow in roleCheckboxRows) 
            {
                roleCheckboxRow.isSelected = false;
            }
        }

        public void UserUpdate(User newUser)
        {
            User toUpdate = _mainViewModel.userCollection.First(u => newUser.id == u.id);
            if (toUpdate != null)
            {
                int indexToUpdate = _mainViewModel.userCollection.IndexOf(toUpdate);
                Application.Current.Dispatcher.Invoke(new System.Action(() => _mainViewModel.userCollection[indexToUpdate] = newUser));
            }
            else
            {
                Application.Current.Dispatcher.Invoke(new System.Action(() => _mainViewModel.userCollection.Add(newUser)));
            }
            
        }

        public void RoleUpdate(Role updatedRole)
        {
            Application.Current.Dispatcher.Invoke(new System.Action(() => _mainViewModel.roleCollection.Add(updatedRole)));
            Application.Current.Dispatcher.Invoke(new System.Action(() => roleCheckboxRows.Add(new RoleCheckboxRow(updatedRole, false))));
        }
    }
}
