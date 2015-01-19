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
using System.Collections.ObjectModel;
using CommunicationLib.Model;
using Admin.ViewModel;

namespace Admin.View
{
    /// <summary>
    /// Interaktionslogik für WorkflowUserControl.xaml
    /// </summary>
    public partial class WorkflowUserControl : UserControl
    {
        
        public WorkflowUserControl()
        {
            InitializeComponent();
            

        }
        protected void HandleDoubleClick(object sender, MouseButtonEventArgs e)
        {


            var lvi = sender as ListViewItem;
            if (lvi != null)
            {
                Workflow wv = lvi.DataContext as Workflow;
                WorkflowDiagramViewModel wmv = (WorkflowDiagramViewModel)this.DataContext;
                wmv.actWorkflow = wv;
                wmv.workflowActivity = "";
                if (wmv.showDetails == Visibility.Collapsed)
                {
                    wmv.showDetails = Visibility.Visible;
                }
                else
                {
                    wmv.showDetails = Visibility.Collapsed;
                }
             }
            
        }

        private void DiagramControl_Loaded(object sender, RoutedEventArgs e)
        {

        }
            
        
    }



}
