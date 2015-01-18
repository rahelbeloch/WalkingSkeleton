using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using DiagramDesigner;
using CommunicationLib.Model;
using System.Collections.Specialized;
using System.Collections.ObjectModel;

namespace Admin
{
    /// <summary>
    /// This is passed to the PopupWindow.xaml window, where a DataTemplate is used to provide the
    /// ContentControl with the look for this data. This class is also used to allow
    /// the popup to be cancelled without applying any changes to the calling ViewModel
    /// whos data will be updated if the PopupWindow.xaml window is closed successfully
    /// </summary>
    public class ActionData: INPCBase
    {
        
        private Role _selectedRole = new Role();
        private String _description = "";
        public ObservableCollection<Role> roleCollection { get; private set; }

        public ActionData(String description, Role currentRole, ObservableCollection<Role> roleCollection )
        {
            _selectedRole = currentRole;
            _description = description;
            this.roleCollection = roleCollection;
        }

        
        public Role selectedRole
        {
            get
            {
                return _selectedRole;
            }
            set
            {
                _selectedRole = value;
                NotifyChanged("selectedRole");
            }
        }
        public String description
        {
            get
            {
                return _description;
            }
            set
            {
                _description = value;
                NotifyChanged("description");
            }
        }
    }
}
