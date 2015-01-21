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
    
    public class ActionViewModel : DesignerItemViewModelBase
    {
       

        //WorkflowDiagramViewModel workflowViewModel = null;
        //public ObservableCollection<Role> roleCollection { get { return workflowViewModel.roleCollection; } }

        public ActionViewModel(string id, DiagramViewModel parent, double left, double top, Role selectedRole)
            : base(id, parent, left, top)
        {
            this.selectedRole = selectedRole;
            Init();
        }

        public ActionViewModel()
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
        private String _description = "";
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
        
        


        private void Init()
        {
            enableInputConnector = true;
            enableRightConnector = true;
            itemWidth = 100;
            itemHeight = 52;
            
            this.ShowConnectors = false;

        }
    }
}
