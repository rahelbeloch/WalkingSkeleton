using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using System.Linq;
using System.Threading.Tasks;
using System.Windows;
using Client.ViewModel;

namespace Client
{
    /// <summary>
    /// Interactive logic for "App.xaml".
    /// </summary>
    public partial class App : Application
    {
        /// <summary>
        /// Method to start the MainWindow of client.
        /// </summary>
        /// <param name="e"></param>
        protected override void OnStartup(StartupEventArgs e)
        {
            base.OnStartup(e);

            MainWindow app = new MainWindow();
            MainViewModel context = new MainViewModel();
            app.DataContext = context;
            app.Show();
        }
    }
}
