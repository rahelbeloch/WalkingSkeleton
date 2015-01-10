using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using CommunicationLib.Model;
using System.Windows.Input;
using CommunicationLib.Exception;
using System.Windows;
using CommunicationLib;
using NLog;

namespace Admin.ViewModel
{
    /// <summary>
    /// ViewModel class for the Login
    /// </summary>
    public class LoginViewModel : ViewModelBase
    {
        private readonly Logger logger = LogManager.GetCurrentClassLogger();
        private MainViewModel _mainViewModel;
        public LoginViewModel(MainViewModel mainViewModelInstanz)
            : base()
        {
            _mainViewModel = mainViewModelInstanz;
        }
        public string Name
        {
            get
            {
                return "Login Model";
            }
        }
        /// <summary>
        /// Property for input from username text box.
        /// </summary>
        private String _securePwd = "";
        public String securePwd
        {
            get
            {
                return _securePwd;
            }
            set
            {
                _securePwd = value;
                OnChanged("securePwd");
            }
        }
        /// <summary>
        /// Property for input from username text box.
        /// </summary>
        private string _admin = "";
        public string admin
        {
            get
            {
                return _admin;
            }
            set
            {
                _admin = value;
                OnChanged("admin");
            }
        }
        /// <summary>
        /// ICommand which is called by the login button
        /// </summary>
        private ICommand _authenticate;
        public ICommand authenticate
        {
            get
            {
                if (_authenticate == null)
                {

                    _authenticate = new ActionCommand(execute =>
                    {
                        try
                        {
                            // Register mainViewModel to CommunicationLib (if login worked)
                            _mainViewModel.myComLib.Login(admin, securePwd);
                            logger.Debug("Authentiaction userName: " + admin);
                            _mainViewModel.InitModel();

                            _mainViewModel.CurrentPageViewModel = _mainViewModel.workflowViewModel;
                            _mainViewModel.admin = _admin;
                        }
                        catch (BasicException exc)
                        {
                            logger.Debug("Login fehlgeschlagen:");

                            MessageBox.Show(exc.Message);
                            Console.WriteLine(exc.ToString());
                        }
                        finally
                        {

                        }
                    }, canExecute =>
                    {
                        return true;
                    });
                }
                return _authenticate;
            }
        }
    }
}
