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
    public class ToolBoxViewModel
    {
        private List<ToolBoxData> toolBoxItems = new List<ToolBoxData>();

        public ToolBoxViewModel()
        {
            toolBoxItems.Add(new ToolBoxData("../Images/Start.png", typeof(StartStepViewModel)));
            toolBoxItems.Add(new ToolBoxData("../Images/Action.png", typeof(ActionViewModel)));
            toolBoxItems.Add(new ToolBoxData("../Images/End.png", typeof(FinalStepViewModel)));
        }

        public List<ToolBoxData> ToolBoxItems
        {
            get { return toolBoxItems; }
        }
    }
}
