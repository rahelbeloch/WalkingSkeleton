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
    /// <summary>
    /// Class for the ActionViewModel
    /// </summary>
    public class ActionViewModel : DesignerItemViewModelBase
    {
       

        //WorkflowDiagramViewModel workflowViewModel = null;
        //public ObservableCollection<Role> roleCollection { get { return workflowViewModel.roleCollection; } }

        /// <summary>
        /// Constructor for the ActionViewModel
        /// </summary>
        /// <param name="id"></param>
        /// <param name="parent"></param>
        /// <param name="left"></param>
        /// <param name="top"></param>
        /// <param name="selectedRole"></param>
        public ActionViewModel(string id, DiagramViewModel parent, double left, double top, Role selectedRole)
            : base(id, parent, left, top)
        {
            this.selectedRole = selectedRole;
            Init();
        }

        /// <summary>
        /// Constructor for the ActionViewModel
        /// </summary>
        public ActionViewModel()
            : base()
        {
            Init();
            
        }

        /// <summary>
        /// Property for the selected Role
        /// </summary>
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
        private Role _selectedRole = null;

        /// <summary>
        /// Property for the description
        /// </summary>
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
        private String _description = "";
        

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
