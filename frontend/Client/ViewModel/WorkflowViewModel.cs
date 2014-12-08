using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Collections.Specialized;
using System.Collections.ObjectModel;
using System.Windows.Input;
using System.Text.RegularExpressions;
using CommunicationLib.Model;
using CommunicationLib;
using System.Windows;
using RestAPI;

namespace Client.ViewModel
{
    /// <summary>
    /// The WorkflowViewModel contains properties and commands to create a new workflow and to send it to the server.
    /// Furthermore, the properties and commands are used as DataBindings in the graphical user interface.
    /// </summary>
    public class WorkflowViewModel : ViewModelBase
    {
        private MainViewModel _mainViewModel;
        public WorkflowViewModel(MainViewModel mainViewModelInstanz)
        {
            _mainViewModel = mainViewModelInstanz;
        }
        private String _userName="";
        public String userName
        {
            get { return _userName; }
            set {
                _userName = value;
                Console.WriteLine("Workflows holen");

                if (_userName.Equals(""))
                {
                    _workflows.Clear();
                }
                else
                {
                    try
                    {
                        _workflows.Clear();
                        RestAPI.RestRequester.GetAllObjects<Workflow>(userName).ToList().ForEach(_workflows.Add);
                        Console.WriteLine("workflows updated");
                        OnChanged("workflows");
                    } catch (Exception e) {
                        Console.WriteLine(e.ToString());
                    }
                    Console.WriteLine("Workflows holen");
                }
            }
        }
        public void createWorkflow(int id, String userName)
        {
            try
            {
                RestRequester.StartWorkflow(id, userName);
            }
            catch (Exception exc)
            {
                Console.WriteLine(exc.ToString());
                throw;
            }
        }
        public void stepForward(int stepId, int itemId, String userName)
        {
            RestRequester.StepForward(stepId, itemId, userName);
        }
        private ICommand _logoutCommand;
        public ICommand logoutCommand
        {
            get
            {
                if (_logoutCommand == null)
                {
                    _logoutCommand = new ActionCommand(excute =>
                        {
                            Console.WriteLine("button clicked");
                            userName = "";
                            _mainViewModel.CurrentPageViewModel = _mainViewModel.loginViewModel;
                            OnChanged("workflows");
                        }, canExecute =>
                            {
                                return true;
                            });
                }
                return _logoutCommand;
            }
        }

        #region properties
        private ObservableCollection<Workflow> _workflows = new ObservableCollection<Workflow>();
        public ObservableCollection<Workflow> workflows { get { return _workflows; } }

        #endregion
    }
}
