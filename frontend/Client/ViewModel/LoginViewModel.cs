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
using System.Diagnostics;
using System.Configuration;
using System.Reflection;
using System.Text.RegularExpressions;

namespace Client.ViewModel
{
    /// <summary>
    /// ViewModel class for the Login.
    /// </summary>
    public class LoginViewModel : ViewModelBase
    {
        private readonly Logger logger = LogManager.GetCurrentClassLogger();
        private MainViewModel _mainViewModel;

        /// <summary>
        /// Constructor for LoginViewModel, with the parent MainViewModel.
        /// </summary>
        /// <param name="mainViewModelInstanz"></param>
        public LoginViewModel(MainViewModel mainViewModelInstanz)
            : base()
        {
            _mainViewModel = mainViewModelInstanz;
            _serverAddress = ConfigurationManager.AppSettings[Constants.SERVER_ADDRESS_NAME];
            _brokerAddress = ConfigurationManager.AppSettings[Constants.BROKER_ADDRESS_NAME];
        }

        /// <summary>
        /// The name of this model.
        /// </summary>
        public string name { get { return _name; } }
        private string _name = "Login Model";
        
        /// <summary>
        /// Property for input from username text box.
        /// </summary>
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
        private String _securePwd = "";
        
        /// <summary>
        /// Property for input from username text box.
        /// </summary>
        public string username
        {
            get
            {
                return _username;
            }
            set
            {
                _username = value;
                OnChanged("username");
            }
        }
        private string _username = "";
        
        /// <summary>
        /// Property for server adress.
        /// </summary>
        public string ServerAddress
        {
            get
            {
                return _serverAddress;
            }
            set
            {
                if (value != null)
                {
                    _serverAddress = value;
                    OnChanged("serverAddress");
                } 
            }
        }
        private string _serverAddress = "";
        
        /// <summary>
        /// Property for broker adress.
        /// </summary>
        public string BrokerAddress
        {
            get
            {
                return _brokerAddress;
            }
            set
            {
                if (value != null)
                {
                    _brokerAddress = value;
                    OnChanged("brokerAddress");
                }
            }
        }
        private string _brokerAddress = "";
        
        /// <summary>
        /// ICommand which is called by the login button.
        /// </summary>
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
                            string oldServerAddr = ConfigurationManager.AppSettings[Constants.SERVER_ADDRESS_NAME];
                            string oldBrokAddr = ConfigurationManager.AppSettings[Constants.BROKER_ADDRESS_NAME];

                            SaveAddresses();

                            if (_mainViewModel.myComLib != null)
                            {
                                if (oldServerAddr == null || !oldServerAddr.Equals(_serverAddress))
                                {
                                    _mainViewModel.myComLib.RefreshServer(_serverAddress);
                                }

                                if (oldBrokAddr == null || !oldBrokAddr.Equals(_brokerAddress))
                                {
                                    _mainViewModel.myComLib.RefreshBroker(_brokerAddress);
                                }

                                // Register mainViewModel to CommunicationLib (if login worked)
                                _mainViewModel.myComLib.Login(username, securePwd);
                                logger.Info("Login successful for username=" + username);
                                _mainViewModel.CurrentPageViewModel = _mainViewModel.dashboardViewModel;
                                _mainViewModel.username = _username;
                                securePwd = "";
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
                                logger.Info("Login failed for username=" + username);
                            }
                            MessageBox.Show(exc.Message);
                        }
                    }, canExecute => username.Length > 0);
                }
                return _authenticate;
            }
        }
        private ICommand _authenticate;
        
        private void SaveAddresses()
        {
            // retrieve local config file path from filesystem
            string uriPath = new Uri(Assembly.GetExecutingAssembly().CodeBase).LocalPath;

            // make use of util class in CommunicationLib
            CommunicationLib.Util.SaveAddressesToConfig(uriPath, _brokerAddress, _serverAddress);
        }
    }
}