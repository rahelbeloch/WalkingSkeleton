using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Data;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Imaging;
using System.Windows.Navigation;
using System.Windows.Shapes;
using Client.ViewModel;
using System.ComponentModel;
using System.Collections.ObjectModel;
using CommunicationLib.Model;
using RestAPI;
using Action = CommunicationLib.Model.Action;

namespace Client.View
{   
    /// <summary>
    /// Interactive logic for WorkflowUserControl.xaml.
    /// </summary>
    public partial class DashboardUserControl : UserControl
    {
        /// <summary>
        /// Constructor for DashboardUserControl.
        /// </summary>
        public DashboardUserControl()
        {
            InitializeComponent();
        }
    }
}
