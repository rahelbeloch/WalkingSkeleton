using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using DiagramDesigner;
using System.Windows.Input;
using CommunicationLib.Model;

namespace Admin
{
    public class FinalStepViewModel : DesignerItemViewModelBase, ISupportDataChanges
    {
        private IUIVisualizerService visualiserService;

        public FinalStepViewModel(string id, DiagramViewModel parent, double left, double top) : base(id,parent, left,top)
        {
           Init();
        }

        public FinalStepViewModel()
            : base()
        {
            Init();
        }

        

        public ICommand ShowDataChangeWindowCommand { get; private set; }

        public void ExecuteShowDataChangeWindowCommand(object parameter)
        {
            FinalStepData data = new FinalStepData();
            
        }


        private void Init()
        {
            enableInputConnector = true;
            itemWidth = 66;
            itemHeight = 66;
            visualiserService = ApplicationServicesProvider.Instance.Provider.VisualizerService;
            ShowDataChangeWindowCommand = new SimpleCommand(ExecuteShowDataChangeWindowCommand);
            this.ShowConnectors = false;

        }
    }
}
