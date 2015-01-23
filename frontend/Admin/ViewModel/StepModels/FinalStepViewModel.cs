using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using DiagramDesigner;
using System.Windows.Input;
using CommunicationLib.Model;

namespace Admin
{
    /// <summary>
    /// Class for FinalStepViewModel.
    /// </summary>
    public class FinalStepViewModel : DesignerItemViewModelBase
    {
        /// <summary>
        /// Constructor for the FinalStepViewModel.
        /// </summary>
        /// <param name="id"></param>
        /// <param name="parent"></param>
        /// <param name="left"></param>
        /// <param name="top"></param>
        public FinalStepViewModel(string id, DiagramViewModel parent, double left, double top) : base(id, parent, left,top)
        {
            Init();
        }

        /// <summary>
        /// Default Constructor.
        /// </summary>
        public FinalStepViewModel()
            : base()
        {
            Init();
        }
       
        private void Init()
        {
            enableInputConnector = true;
            itemWidth = 66;
            itemHeight = 66;
            
            this.ShowConnectors = false;
        }
    }
}