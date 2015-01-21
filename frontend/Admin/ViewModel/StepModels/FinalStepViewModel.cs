using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using DiagramDesigner;
using System.Windows.Input;
using CommunicationLib.Model;

namespace Admin
{
    public class FinalStepViewModel : DesignerItemViewModelBase
    {
        

        public FinalStepViewModel(string id, DiagramViewModel parent, double left, double top) : base(id,parent, left,top)
        {
           Init();
        }

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
