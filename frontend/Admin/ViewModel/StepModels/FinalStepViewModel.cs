using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using DiagramDesigner;
using System.Windows.Input;


namespace Admin
{
    public class FinalStepViewModel : DesignerItemViewModelBase, ISupportDataChanges
    {
        private IUIVisualizerService visualiserService;

        public FinalStepViewModel(int id, DiagramViewModel parent, double left, double top, string hostUrl) : base(id,parent, left,top)
        {
            this.HostUrl = hostUrl;
            Init();
        }

        public FinalStepViewModel()
            : base()
        {
            Init();
        }


        public String HostUrl { get; set; }
        public ICommand ShowDataChangeWindowCommand { get; private set; }

        public void ExecuteShowDataChangeWindowCommand(object parameter)
        {
            FinalStepData data = new FinalStepData(HostUrl);
            if (visualiserService.ShowDialog(data) == true)
            {
                this.HostUrl = data.HostUrl;
            }
        }


        private void Init()
        {
            enableInputConnector = true;
            visualiserService = ApplicationServicesProvider.Instance.Provider.VisualizerService;
            ShowDataChangeWindowCommand = new SimpleCommand(ExecuteShowDataChangeWindowCommand);
            this.ShowConnectors = false;

        }
    }
}
