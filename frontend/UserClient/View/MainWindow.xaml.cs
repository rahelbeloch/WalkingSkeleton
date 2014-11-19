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
using UserClient.ViewModel;
using UserClient.Model;

namespace UserClient
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
        private void Login_Click(object sender, RoutedEventArgs e)
        {
            LoginLayer.Visibility = Model.Authentication.Authenticate1(txtName.Text, txtPassword.SecurePassword) ? Visibility.Collapsed : Visibility.Visible;
        }
        private void Log_Out(object sender, RoutedEventArgs e)
        {
            LoginLayer.Visibility = Visibility.Visible;
        }
    }
}
