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

        public LoginViewModel(MainViewModel mainViewModelInstanz)
            : base()
        {
            _mainViewModel = mainViewModelInstanz;
            _serverAddress = ConfigurationManager.AppSettings["ServerAddress"];
            _brokerAddress = ConfigurationManager.AppSettings["BrokerAddress"];
        }

        private string _name = "Login Model";
        public string name { get { return _name; } }

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
        private string _username = "";
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
                _serverAddress = value;
                OnChanged("serverAddress");
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
                _brokerAddress = value;
                OnChanged("brokerAddress");
            }
        }

        /// <summary>
        /// ICommand which is called by the login button.
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
                            SaveAddresses();
                            _mainViewModel.myComLib.RefreshUrls(_serverAddress, _brokerAddress);

                            // Register mainViewModel to CommunicationLib (if login worked)
                            _mainViewModel.myComLib.Login(username, securePwd);
                            logger.Info("Login successful for username=" + username + " password=" + securePwd);
                            _mainViewModel.CurrentPageViewModel = _mainViewModel.dashboardViewModel;
                            _mainViewModel.username = _username;
                        }
                        catch (BasicException exc)
                        {
                            if (exc.GetType() == typeof(InvalidAddressException))
                            {
                                logger.Info("Connection to server failed because of invalid address.");
                            }
                            else
                            {
                                logger.Info("Login failed for username=" + username + " password=" + securePwd);
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
            string server = "http";
            string broker = "tcp";

            // retrieve local config file path from filesystem
            string uriPath = new Uri(Assembly.GetExecutingAssembly().CodeBase).LocalPath;
            // save server and broker address to config file
            Configuration config = ConfigurationManager.OpenExeConfiguration(uriPath);

            // test if given addresses are valid
            if (ValidateAddress(server, _serverAddress) && ValidateAddress(broker, _brokerAddress))
            {
                if (config.AppSettings.Settings["ServerAddress"] == null && config.AppSettings.Settings["BrokerAddress"] == null)
                {
                    config.AppSettings.Settings.Add("ServerAddress", _serverAddress);
                    config.AppSettings.Settings.Add("BrokerAddress", _brokerAddress);
                }
                else
                {
                    config.AppSettings.Settings["ServerAddress"].Value = _serverAddress;
                    config.AppSettings.Settings["BrokerAddress"].Value = _brokerAddress;
                }
            }
            else
            {
                throw new InvalidAddressException();
            }

            config.Save(ConfigurationSaveMode.Modified);
            ConfigurationManager.RefreshSection("appSettings");
        }

        private bool ValidateAddress(string pre, string matchString)
        {
            string ipPattern = pre + "://\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b:[0-9]{4,}";
            string localPattern = pre + "://localhost:[0-9]{4,}";

            // calling regex for ip/local server address
            Match matchIp = Regex.Match(matchString, @ipPattern, RegexOptions.IgnoreCase);
            Match matchLocal = Regex.Match(matchString, @localPattern, RegexOptions.IgnoreCase);

            // check the match success
            return matchIp.Success || matchLocal.Success;
        }
    }
}