using CommunicationLib;
using CommunicationLib.Exception;
using CommunicationLib.Model;
using CommunicationLib.Model.DataModel;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;
using System.Windows.Input;

namespace Admin.ViewModel
{
    /// <summary>
    /// The FromViewModel contains properties and commands for sending and updating forms.
    /// </summary>
    public class FormViewModel:ViewModelBase
    {
        private MainViewModel _mainViewModel;
        private IRestRequester _restRequester;

        public FormViewModel(MainViewModel mainViewModel)
        {
            _mainViewModel = mainViewModel;
            _restRequester = _mainViewModel.restRequester;
        }

        

        /// <summary>
        /// Init the model via rest requests at first startup.
        /// </summary>
        public void InitModel()
        {
            try
            {
                // update formlist by getting a whole new batch
                IList<Form> allForms = _restRequester.GetAllElements<Form>();
                foreach (Form form in allForms)
                {
                    _mainViewModel.formCollection.Add(form);
                }
            }
            catch (BasicException e)
            {
                MessageBox.Show(e.Message);
            }
        }

        #region properties
        public ObservableCollection<Form> formCollection { get { return _mainViewModel.formCollection; } }

        private Form _formModel;
        public Form formModel
        {
            get
            {
                return _formModel;
            }
            set
            {
                _formModel = value;
                OnChanged("formModel");
            }
        }

        //private Dictionary<String, String> _formDefModel = null;
        public List<FormEntry> formDefModel { get; set; }
        #endregion
        #region commands

        private ICommand _addFormCommand;
        public ICommand addFormCommand
        {
            get
            {
                if (_addFormCommand == null)
                {
                    _addFormCommand = new ActionCommand(execute =>
                    {
                        //TODO: what happens if command is executed
                        formDefModel = new List<FormEntry>();
                        FormEntry formEntry = new FormEntry();
                        formEntry.key = "key";
                        formEntry.value = "value";

                        formDefModel.Add(formEntry);
                        OnChanged("formDefModel");

                    }, canExecute => formDefModel == null);
                }

                return _addFormCommand;
            }

        }
        #endregion

    }
}
