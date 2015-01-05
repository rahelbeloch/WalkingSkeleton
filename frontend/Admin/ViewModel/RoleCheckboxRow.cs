using CommunicationLib.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Admin.ViewModel
{
    public class RoleCheckboxRow : ViewModelBase
    {
        private Role _role;
        public Role Role { get { return _role; } set { _role = value; } }

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
