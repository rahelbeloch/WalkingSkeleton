﻿using System;
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

namespace Client.ViewModel
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
                        try{
                            // Register mainViewModel to CommunicationLib (if login worked)
                            _mainViewModel.myComLib.Login(username, securePwd);
                            logger.Debug("Authentiaction userName: " + username);
                            _mainViewModel.CurrentPageViewModel = _mainViewModel.dashboardViewModel;
                            _mainViewModel.username = _username;
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
