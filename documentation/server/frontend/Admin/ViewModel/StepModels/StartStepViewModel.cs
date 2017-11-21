using System;
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
    /// StartStepViewModel class.
    /// </summary>
    public class StartStepViewModel : DesignerItemViewModelBase
    {
        /// <summary>
        /// Constructor for StartStepViewModel.
        /// </summary>
        public StartStepViewModel(string id, DiagramViewModel parent, double left, double top, Role selectedRole)
            : base(id, parent, left, top)
        {
            this.selectedRole = selectedRole;
            Init();
        }

        /// <summary>
        /// Constructor for StartStepViewModel.
        /// </summary>
        public StartStepViewModel()
            : base()
        {
            Init();
        }

        /// <summary>
        /// Property for the selected Role.
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

        private void Init()
        {
            enableRightConnector = true;
            itemWidth = 50;
            itemHeight = 50;
           
            this.ShowConnectors = false;
        }
    }
}