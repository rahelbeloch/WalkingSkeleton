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

namespace Client.View
{   
    /// <summary>
    /// Interaktionslogik für WorkflowUserControl.xaml
    /// </summary>
    public partial class WorkflowUserControl : UserControl
    {
        private WorkflowViewModel _workflowViewModel; 
        public WorkflowUserControl()
        {
            InitializeComponent();
        }
        private void pageLoaded(object sender, RoutedEventArgs e)
        {
            Console.WriteLine(this.DataContext.ToString());
            _workflowViewModel = (WorkflowViewModel)(this.DataContext);
            _workflowViewModel.PropertyChanged += new PropertyChangedEventHandler(SubPropertyChanged);
        }
        private void SubPropertyChanged(object sender, PropertyChangedEventArgs e)
        {
            if (e.PropertyName == "workflows")
            {
                Console.WriteLine("workflows changed");
            }
        }
    }
}
