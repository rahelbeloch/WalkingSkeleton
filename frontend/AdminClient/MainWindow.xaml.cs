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

namespace AdminClient
{
    /// <summary>
    /// Interaktionslogik für MainWindow.xaml
    /// </summary>
    public partial class MainWindow : Window
    {
        public MainWindow()
        {
            InitializeComponent();
        }

        /// <summary>
        /// This method opens the popup window to choose and element to add for the current workflow.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void OpenAddElementWindow(object sender, RoutedEventArgs e)
        {
            AddElementWindow addElementWindow = new AddElementWindow();
            addElementWindow.Show();
        }
    }
}
