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

            updateModel();
        }

        private void updateModel()
        {
            // update userlist
            IList<User> allUsers = _restRequester.GetAllElements<User>();
            foreach (User user in allUsers)
            {
                userCollection.Add(user);
            }

            // update rolelist
            IList<Role> allRoles =  _restRequester.GetAllElements<Role>();
            foreach(Role role in allRoles)
            {
                // ueberpruefen ob der aktuell ausgewählte Nutzer die Rolle hat
                roleCheckboxRows.Add(new RoleCheckboxRow(role, false));
                roleCollection.Add(role);
            }
        }

        /// <summary>
        /// Property _userCollection to fill list view with users.
        /// </summary>
        private ObservableCollection<User> _userCollection = new ObservableCollection<User>();
        public ObservableCollection<User> userCollection { get { return _userCollection; } }

        /// <summary>
        /// Property _roleCollection to fill list view with users.
        /// </summary>
        private ObservableCollection<Role> _roleCollection = new ObservableCollection<Role>();
        public ObservableCollection<Role> roleCollection { get { return _roleCollection; } }

        /// <summary>
        /// Property _roleCheckboxRoles to fill list view with selectable roles.
        /// </summary>
        private ObservableCollection<RoleCheckboxRow> _roleCheckboxRows = new ObservableCollection<RoleCheckboxRow>();
        public ObservableCollection<RoleCheckboxRow> roleCheckboxRows { get { return _roleCheckboxRows; } }

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
                            User newUser = new User();
                            //newUser.username = username;
                            newUser.id = username;

                            foreach(RoleCheckboxRow actRow in roleCheckboxRows) 
                            {
                                if (actRow.isSelected)
                                {
                                    newUser.roles.Add(actRow.role);
                                }
                            }

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

                            _restRequester.PostObject<Role>(newRole);

                            // update view model
                            _roleCollection.Add(newRole);
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

        void RoleUpdate(Role updatedRole)
        {
            Console.WriteLine("Update UserViewModel: RoleID = " + updatedRole.id);
        }
    }
}
