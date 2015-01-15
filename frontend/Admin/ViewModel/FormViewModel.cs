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
            formDefModel = new ObservableCollection<FormEntry>();
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

        public ObservableCollection<FormEntry> formDefModel { get; set; }

        private String _visible = "Hidden";
        public String visible
        {
            get
            {
                return _visible;
            }
            set
            {
                _visible = value;
                OnChanged("visible");
            }
        }

        private String _formDefModelId = "";
        public String formDefModelId
        {
            get
            {
                return _formDefModelId;
            }
            set
            {
                _formDefModelId = value;
                OnChanged("formDefModelId");
            }
        }

        private String _formDefModelDescription = "";
        public String formDefModelDescription
        {
            get
            {
                return _formDefModelDescription;
            }
            set
            {
                _formDefModelDescription = value;
                OnChanged("formDefModelDescription");
            }

        }
        #endregion

        #region methods

        public void updateForm(Form form)
        {
            _mainViewModel.formCollection.Add(form);
        }
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
                        
                        FormEntry formEntry = new FormEntry();
                        formEntry.key = "";
                        formEntry.value = "";
                        formDefModel.Add(formEntry);
                        OnChanged("formDefModel");

                        _visible = "Visible";
                        OnChanged("visible");

                    }, canExecute => formDefModel.Count == 0);
                }

                return _addFormCommand;
            }

        }

        private ICommand _addDefinitionCommand;
        public ICommand addDefinitionCommand
        {
            get
            {
                if (_addDefinitionCommand == null)
                {
                    _addDefinitionCommand = new ActionCommand(execute =>
                    {
                        if (formDefModel.ElementAt(formDefModel.Count - 1).key != "" && formDefModel.ElementAt(formDefModel.Count - 1).value != "")
                        {
                            FormEntry formEntry = new FormEntry();
                            formEntry.key = "";
                            formEntry.value = "";
                            formDefModel.Add(formEntry);
                            OnChanged("formDefModel");
                        }


                    }, canExecute => formDefModel.Count != 0);
                }

                return _addDefinitionCommand;
            }
        }

        private ICommand _submitFormCommand;
        public ICommand submitFormCommand
        {
            get
            {
                if (_submitFormCommand == null)
                {
                    _submitFormCommand = new ActionCommand(execute =>
                    {
                        //TODO: was passiert beim runterschicken von formularen
                        Form form = new Form();

                        foreach (FormEntry fe in formDefModel)
                        {
                            FormEntry formEntry = new FormEntry();
                            formEntry.id = fe.key;
                            formEntry.value = fe.value;
                            form.formDef.Add(formEntry);
                        }

                        form.id = _formDefModelId;
                        form.description = _formDefModelDescription;

                        _restRequester.PostObject(form);
                        formDefModel.Clear();
                        _visible = "Hidden";
                        OnChanged("formDefModel");
                        OnChanged("visible");
                    }, canExecute => 
                    {
                        if (formDefModel.Count != 0)
                        {
                            foreach (FormEntry fe in formDefModel)
                            {
                                if (fe.key == "" || fe.value == "")
                                {
                                    return false;
                                }
                            }
                            if (_formDefModelId == ""){
                                return false;
                            }
                        
                            return true;
                        }
                        return false;
                    });
                }
                return _submitFormCommand;
            }
        }

        private ICommand _resetFormCommand;
        public ICommand resetFormCommand
        {
            get
            {
                if (_resetFormCommand == null)
                {
                    _resetFormCommand = new ActionCommand(execute =>
                        {
                            formDefModel = null;
                            _visible = "Hidden";
                            OnChanged("formDefModel");
                            OnChanged("visible");
                        }, canExecute => formDefModel.Count != 0);
                }
                return _resetFormCommand;
            }
        }
        #endregion
    }
}
