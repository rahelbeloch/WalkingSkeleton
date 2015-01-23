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
    public class ForkViewModel : DesignerItemViewModelBase
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
        public ForkViewModel(string id, DiagramViewModel parent, double left, double top, String description)
            : base(id, parent, left, top)
        {
            this._description = description;
            Init();
        }

        /// <summary>
        /// Constructor for the ActionViewModel
        /// </summary>
        public ForkViewModel()
            : base()
        {
            Init();
            
        }


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
            itemWidth = 80;
            itemHeight = 80;
            
            this.ShowConnectors = false;

        }
    }
}
