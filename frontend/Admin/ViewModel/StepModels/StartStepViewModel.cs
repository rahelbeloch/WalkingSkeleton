using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using DiagramDesigner;
using System.Windows.Input;


namespace Admin
{
    public class StartStepViewModel : DesignerItemViewModelBase, ISupportDataChanges
    {
        private IUIVisualizerService visualiserService;

        public StartStepViewModel(int id, DiagramViewModel parent, double left, double top, string hostUrl) : base(id,parent, left,top)
        {
            this.HostUrl = hostUrl;
            Init();
        }

        public StartStepViewModel()
            : base()
        {
            Init();
        }


        public String HostUrl { get; set; }
        public ICommand ShowDataChangeWindowCommand { get; private set; }

        public void ExecuteShowDataChangeWindowCommand(object parameter)
        {
            StartStepData data = new StartStepData(HostUrl);
            if (visualiserService.ShowDialog(data) == true)
            {
                this.HostUrl = data.HostUrl;
            }
        }


        private void Init()
        {
            enableRightConnector = true;
            itemWidth = 50;
            itemHeight = 50;
            visualiserService = ApplicationServicesProvider.Instance.Provider.VisualizerService;
            ShowDataChangeWindowCommand = new SimpleCommand(ExecuteShowDataChangeWindowCommand);
            this.ShowConnectors = false;

        }
    }
}
