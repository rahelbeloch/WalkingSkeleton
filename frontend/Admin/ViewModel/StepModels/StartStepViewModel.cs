using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using DiagramDesigner;
using System.Windows.Input;
using CommunicationLib.Model;
using System.Collections.Specialized;
using System.Collections.ObjectModel;
using Admin.ViewModel;

namespace Admin
{
    public class StartStepViewModel : DesignerItemViewModelBase
    {
        public StartStepViewModel(string id, DiagramViewModel parent, double left, double top, Role selectedRole)
            : base(id, parent, left, top)
        {
            this.selectedRole = selectedRole;
            Init();
        }

        public StartStepViewModel()
            : base()
        {
            Init();
        }


        private Role _selectedRole = null;
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
        

        private void Init()
        {
            enableRightConnector = true;
            itemWidth = 50;
            itemHeight = 50;
           
            this.ShowConnectors = false;
        }
    }
}
