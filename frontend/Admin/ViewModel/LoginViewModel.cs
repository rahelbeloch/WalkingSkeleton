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
    /// ViewModel class for the Login.
    /// </summary>
    public class LoginViewModel : ViewModelBase
    {
        private MainViewModel _mainViewModel;
        private static Logger logger = LogManager.GetCurrentClassLogger();
        
        public LoginViewModel(MainViewModel mainViewModel)
            : base()
        {
            _mainViewModel = mainViewModel;
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
                            _mainViewModel.InitModel();

                            _mainViewModel.CurrentPageViewModel = _mainViewModel.workflowViewModel;
                            _mainViewModel.admin = _admin;
                        }
                        catch (BasicException exc)
                        {
                            MessageBox.Show(exc.Message);
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
