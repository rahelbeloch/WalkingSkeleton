using CommunicationLib.Model;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Client.ViewModel
{
    /// <summary>
    /// ViewModel class which holds the data for one workflow in the view.
    /// </summary>
    public class DashboardWorkflow
    {
        private string _name;
        public string name { get { return _name; } set { _name = value; } }
        
        private Workflow _actWorkflow;
        public Workflow actWorkflow { get { return _actWorkflow; } }
        
        private ObservableCollection<DashboardRow> _dashboardRows;
        public ObservableCollection<DashboardRow> dashboardRows { get { return _dashboardRows; } }
        
        private Boolean _startPermission;
        public Boolean startPermission { get { return _startPermission; } set { _startPermission = value; } }
        
        public DashboardWorkflow(Workflow actWorkflow)
        {
            this._actWorkflow = actWorkflow;
            name = "Workflow " + actWorkflow.id;
            this._dashboardRows = new ObservableCollection<DashboardRow>();
        }

        public void AddDashboardRow(DashboardRow newRow)
        {
            _dashboardRows.Add(newRow);
        }
    }
}