using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Collections.ObjectModel;
using DiagramDesigner.Helpers;
using DiagramDesigner;
using Admin.ViewModel;

namespace Admin
{
    /// <summary>
    /// ToolBoxViewModel class
    /// </summary>
    public class ToolBoxViewModel
    {
        private List<ToolBoxData> toolBoxItems = new List<ToolBoxData>();

        /// <summary>
        /// Constructor for ToolBoxViewModel
        /// </summary>
        public ToolBoxViewModel()
        {
            toolBoxItems.Add(new ToolBoxData("../Images/Start.png", typeof(StartStepViewModel)));
            toolBoxItems.Add(new ToolBoxData("../Images/Action.png", typeof(ActionViewModel)));
            toolBoxItems.Add(new ToolBoxData("../Images/End.png", typeof(FinalStepViewModel)));
        }

        /// <summary>
        /// Getter for ToolBoxItems
        /// </summary>
        public List<ToolBoxData> ToolBoxItems
        {
            get { return toolBoxItems; }
        }
    }
}
