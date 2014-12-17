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
        public Role role { get { return _role; } set { _role = value; } }

        private Boolean _isSelected;
        public Boolean isSelected 
        { 
            get 
            { 
                return _isSelected; 
            } 
            set 
            { 
                _isSelected = value; 
                OnChanged("isSelected"); 
            } 
        }

        public RoleCheckboxRow(Role role, Boolean isSelected)
        {
            this.role = role;
            this.isSelected = isSelected;
        }
    }
}
