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
using System.Configuration;
using System.Reflection;
using System.Text.RegularExpressions;

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
            _serverAddress = ConfigurationManager.AppSettings[Constants.SERVER_ADDRESS_NAME];
            _brokerAddress = ConfigurationManager.AppSettings[Constants.BROKER_ADDRESS_NAME];
            Console.WriteLine(_serverAddress);
            Console.WriteLine(_brokerAddress);
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
        /// Property for server adress.
        /// </summary>
        private string _serverAddress;
        public string ServerAddress
        {
            get
            {
                return _serverAddress;
            }
            set
            {
                if (_serverAddress != value)
                {
                    _serverAddress = value;

                    if (_mainViewModel.myComLib != null)
                    {
                        // refresh ONLY serverAddress in ComLib
                        _mainViewModel.myComLib.RefreshServer(_serverAddress);
                    }

                    OnChanged("serverAddress");
                }          
            }
        }

        /// <summary>
        /// Property for broker adress.
        /// </summary>
        private string _brokerAddress;
        public string BrokerAddress
        {
            get
            {
                return _brokerAddress;
            }
            set
            {
                if (_brokerAddress != value)
                {
                    _brokerAddress = value;

                    if (_mainViewModel.myComLib != null)
                    {
                        // refresh ONLY broker in ComLib
                        _mainViewModel.myComLib.RefreshBroker(_brokerAddress);
                    }

                    OnChanged("brokerAddress");
                }
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
                            // save server and broker address to config file (admin.exe)
                            SaveAddresses();

                            if (_mainViewModel.myComLib != null)
                            {
                                // Register mainViewModel to CommunicationLib (if login worked)
                                _mainViewModel.myComLib.Login(admin, securePwd);
                                _mainViewModel.InitModel();

                                _mainViewModel.CurrentPageViewModel = _mainViewModel.workflowViewModel;
                                _mainViewModel.admin = _admin;
                            }
                        }
                        catch (BasicException exc)
                        {
                            if (exc.GetType() == typeof(InvalidAddressException))
                            {
                                logger.Info("Connection to server failed because of invalid address.");
                            }
                            else
                            {
                                logger.Info("Login failed for username=" + _admin + " password=" + securePwd);
                            }
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

        private void SaveAddresses()
        {
            // retrieve local config file path from filesystem
            string uriPath = new Uri(Assembly.GetExecutingAssembly().CodeBase).LocalPath;

            // make use of util class in CommunicationLib
            CommunicationLib.Util.SaveAddressesToConfig(uriPath, _brokerAddress, _serverAddress);
        }
    }
}