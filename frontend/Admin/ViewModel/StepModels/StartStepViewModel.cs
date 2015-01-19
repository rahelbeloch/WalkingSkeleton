﻿using System;
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
    public class StartStepViewModel : DesignerItemViewModelBase, ISupportDataChanges
    {
        private IUIVisualizerService visualiserService;
        WorkflowDiagramViewModel workflowViewModel = null;
        public ObservableCollection<Role> roleCollection { get { return workflowViewModel.roleCollection; } }

        public StartStepViewModel(string id, DiagramViewModel parent, double left, double top, Role selectedRole)
            : base(id, parent, left, top)
        {
            this.selectedRole = selectedRole;
            Init();
        }

        public StartStepViewModel()
            : base()
        {
            Init();
        }


        private Role _selectedRole = new Role();
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
        public ICommand ShowDataChangeWindowCommand { get; private set; }

        public void ExecuteShowDataChangeWindowCommand(object parameter)
        {
            if (this.workflowViewModel == null)
            {
                this.workflowViewModel = (WorkflowDiagramViewModel)this.Parent.workflowViewModel;
            }
            StartStepData data = new StartStepData(selectedRole, roleCollection);
            if (visualiserService.ShowDialog(data) == true)
            {
                this.selectedRole = data.selectedRole;
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
