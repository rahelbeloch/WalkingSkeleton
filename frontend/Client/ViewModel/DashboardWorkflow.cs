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
        /// <summary>
        /// The displayed header for the workflow
        /// </summary>
        public string name { get { return _name; } set { _name = value; } }
        
        private Workflow _actWorkflow;
        /// <summary>
        /// the model of the workflow from the server
        /// </summary>
        public Workflow actWorkflow { get { return _actWorkflow; } }
        
        private ObservableCollection<DashboardRow> _dashboardRows;
        /// <summary>
        /// Generated rows of the displayed workflow
        /// </summary>
        public ObservableCollection<DashboardRow> dashboardRows { get { return _dashboardRows; } }
        
        private Boolean _startPermission;
        /// <summary>
        /// Does the user has the permission to start the current workflow
        /// </summary>
        public Boolean startPermission { get { return _startPermission; } set { _startPermission = value; } }
        
        public DashboardWorkflow(Workflow actWorkflow)
        {
            this._actWorkflow = actWorkflow;
            name = "Workflow " + actWorkflow.id;
            this._dashboardRows = new ObservableCollection<DashboardRow>();
        }
        /// <summary>
        /// Adds an new DashboardRow to the ObservableCollection dashboardRows
        /// </summary>
        /// <param name="newRow">the new DashboardRow</param>
        public void AddDashboardRow(DashboardRow newRow)
        {
            _dashboardRows.Add(newRow);
        }
        /// <summary>
        /// Deletes the DashboardRow from the ObservableCollection dashboardRows
        /// </summary>
        /// <param name="oldRow">The old DashboardRow</param>
        public void DeleteDashboardRow(DashboardRow oldRow)
        {
            _dashboardRows.Remove(oldRow);
        }
    }
}