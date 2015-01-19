using CommunicationLib.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Admin.ViewModel
{
    /// <summary>
    /// This class is used for as a DataBinding for a row in a ListView Control.
    /// </summary>
    public class RoleCheckboxRow : ViewModelBase
    {
        /// <summary>
        /// Property to bind the role to a row.
        /// </summary>
        private Role _role;
        public Role Role { get { return _role; } set { _role = value; } }

        /// <summary>
        /// Property to (de)select the checkbox in a row.
        /// </summary>
        private Boolean _isSelected;
        public Boolean IsSelected 
        { 
            get 
            { 
                return _isSelected; 
            } 
            set 
            { 
                _isSelected = value; 
                OnChanged("IsSelected"); 
            } 
        }

        public RoleCheckboxRow(Role role, Boolean isSelected)
        {
            this.Role = role;
            this.IsSelected = isSelected;
        }
    }
}