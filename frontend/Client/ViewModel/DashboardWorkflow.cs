using CommunicationLib.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Client.ViewModel
{
    class DashboardWorkflow
    {
        private Workflow _actWorkflow;
        private List<DashboardRow> _dashboardRows;
        public List<DashboardRow> dashboardRows { get { return _dashboardRows; } }
        private Boolean _startPermission;
        public Boolean startPermission { get { return _startPermission; } set { _startPermission = value; } }
        public DashboardWorkflow(Workflow actWorkflow)
        {
            this._actWorkflow = actWorkflow;
        }
        public void addDashboardRow(DashboardRow newRow)
        {
            _dashboardRows.Add(newRow);
        }
    }
}
