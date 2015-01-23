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

        /// <summary>
        /// Constructor for the FormViewModel
        /// </summary>
        /// <param name="mainViewModel"></param>
        public FormViewModel(MainViewModel mainViewModel)
        {
            _mainViewModel = mainViewModel;
            formDefModel = new ObservableCollection<FormEntry>();
        }

        #region properties
        /// <summary>
        /// This property is used for the form overview.
        /// </summary>
        public ObservableCollection<Form> formCollection { get { return _mainViewModel.formCollection; } }
        /// <summary>
        /// This property is used if the client defines a new form.
        /// </summary>
        public ObservableCollection<FormEntry> formDefModel { get; set; }

        /// <summary>
        /// This property indicates which definition was selected in the view.
        /// </summary>
        public FormEntry selectedDefinition
        {
            get
            {
                return _selectedDefinition;
            }
            set
            {
                _selectedDefinition = value;
                OnChanged("selectedDefinition");
            }
        }
        private FormEntry _selectedDefinition = null;

        /// <summary>
        /// This property is setted if a form is selected in the overview.
        /// </summary>
        public Form selectedForm
        {
            get
            {
                return _selectedForm;
            }
            set
            {
                _selectedForm = value;
                visibleView = "Visible";
                visibleDefinition = "Collapsed";
                formDefModel.Clear();
                OnChanged("selectedForm");
                OnChanged("formDefModel");
            }
        }
        private Form _selectedForm = null;

        /// <summary>
        /// This property is used as a value setter to hide user controls.
        /// </summary>
        public String visibleDefinition
        {
            get
            {
                return _visibleDefinition;
            }
            set
            {
                _visibleDefinition = value;
                OnChanged("visibleDefinition");
            }
        }
        private String _visibleDefinition = "Collapsed";

        /// <summary>
        /// This property is used as a value setter to hide the form overview.
        /// </summary>
        public String visibleView
        {
            get
            {
                return _visibleView;
            }
            set
            {
                _visibleView = value;
                OnChanged("visibleView");
            }
        }
        private String _visibleView = "Collapsed";

        /// <summary>
        /// This property will be used for setting a form's id.
        /// </summary>
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
        private String _formDefModelId = "";

        /// <summary>
        /// This property will be used for setting a form's description.
        /// </summary>
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
        private String _formDefModelDescription = "";

        #endregion

        #region methods

        /// <summary>
        /// Init the model via rest requests at first startup.
        /// </summary>
        public void InitModel()
        {
            try
            {
                _restRequester = _mainViewModel.restRequester;
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

        /// <summary>
        /// This methods resets viewmodel.
        /// </summary>
        public void ClearModel()
        {
            selectedDefinition = null;
            selectedForm = null;
            visibleDefinition = "Collapsed";
            visibleView = "Collapsed";
            formDefModelId = "";
            formDefModelDescription = "";
            formCollection.Clear();
            formDefModel.Clear();

        }

        /// <summary>
        /// This method is called when the client receives a form from the server.
        /// </summary>
        /// <param name="form">form which will be added to the form collection</param>
        public void UpdateForm(Form form)
        {
            _mainViewModel.formCollection.Add(form);
        }

        /// <summary>
        /// This method is called when the client removes a form.
        /// </summary>
        /// <param name="formId"></param>
        public void FormDeletion(String formId)
        {
            Form formToDelete = null;

            foreach (Form form in formCollection)
            {
                if (form.id.Equals(formId))
                {
                    formToDelete = form;
                }
                
            }
            _mainViewModel.formCollection.Remove(formToDelete);
            
        }
        #endregion

        #region commands

        /// <summary>
        /// This command is executed if the client wants to create a new form.
        /// </summary>
        public ICommand addFormCommand
        {
            get
            {
                if (_addFormCommand == null)
                {
                    _addFormCommand = new ActionCommand(execute =>
                    {
                        
                        if (_selectedForm != null)
                        {
                            selectedForm = null;
                        }
                        formDefModelId = "";
                        formDefModelDescription = "";
                        FormEntry formEntry = new FormEntry();
                        formEntry.key = "";
                        formDefModel.Add(formEntry);
                        visibleView = "Collapsed";
                        visibleDefinition = "Visible";
                        OnChanged("formDefModel");

                    }, canExecute => formDefModel.Count == 0);
                }
                return _addFormCommand;
            }
        }
        private ICommand _addFormCommand;

        /// <summary>
        /// This command is executed if a client wants to delete a form.
        /// </summary>
        public ICommand deleteFormCommand
        {
            get
            {
                if (_deleteFormCommand == null)
                {
                    _deleteFormCommand = new ActionCommand(execute =>
                        {
                            try
                            {
                                _restRequester.DeleteObject<Form>(selectedForm.id);
                            }
                            catch (BasicException be)
                            {
                                if (be is StorageFailedException)
                                {
                                    MessageBox.Show(be.Message + "\n Form konnte nicht gelöscht werden!");
                                }
                                
                            }
                            selectedForm = null;
                            visibleView = "Collapsed";

                        }, canExecute => _selectedForm != null);
                }
                return _deleteFormCommand;
            } 
        }
        private ICommand _deleteFormCommand;

        /// <summary>
        /// This command is executed if the client adds a new definition to a form.
        /// </summary>
        public ICommand addDefinitionCommand
        {
            get
            {
                if (_addDefinitionCommand == null)
                {
                    _addDefinitionCommand = new ActionCommand(execute =>
                    {
                        var lastEle = formDefModel.Last();

                        if (lastEle.key != "" && lastEle.datatype != "" && lastEle.datatype != null)
                        {
                            FormEntry formEntry = new FormEntry();
                            formEntry.key = "";
                            formDefModel.Add(formEntry);
                            OnChanged("formDefModel");
                        }
                        else
                        {
                            MessageBox.Show("Nicht alle Felder des vorherigen Eintrages ausgefüllt!");
                        }

                    }, canExecute => formDefModel.Count != 0);
                }
                return _addDefinitionCommand;
            }
        }
        private ICommand _addDefinitionCommand;

        /// <summary>
        /// This method is used if the client removes a selected definition.
        /// </summary>
        public ICommand removeDefinitionCommand
        {
            get
            {
                if (_removeDefinitionCommand == null)
                {
                    _removeDefinitionCommand = new ActionCommand(execute =>
                        {
                            formDefModel.Remove(_selectedDefinition);
                            if (formDefModel.Count == 0)
                            {
                                formDefModel.Clear();
                                visibleDefinition = "Collapsed";
                            }
                            OnChanged("formDefModel");
                        }, canExecute =>_selectedDefinition != null);
                }
                return _removeDefinitionCommand;
            }
        }
        private ICommand _removeDefinitionCommand;

        /// <summary>
        /// This command is used if the client wants to submit a form to the server.
        /// </summary>
        public ICommand submitFormCommand
        {
            get
            {
                if (_submitFormCommand == null)
                {
                    _submitFormCommand = new ActionCommand(execute =>
                    {
                        Form form = new Form();
                        foreach (FormEntry fe in formDefModel)
                        {
                            FormEntry formEntry = new FormEntry();
                            formEntry.id = fe.key;
                            formEntry.datatype = fe.datatype;
                            form.formDef.Add(formEntry);
                        }
                        form.id = _formDefModelId;
                        form.description = _formDefModelDescription;
                        try
                        {
                            _restRequester.PostObject(form);
                        } catch (BasicException exc) { MessageBox.Show(exc.Message); }
                        formDefModel.Clear();
                        _formDefModelDescription = "";
                        _formDefModelId = "";
                        visibleDefinition = "Collapsed";
                        OnChanged("formDefModel");
                    }, canExecute => 
                    {
                        if (formDefModel.Count != 0)
                        {
                            foreach (FormEntry fe in formDefModel)
                            {
                                if (fe.key == "" || fe.datatype == "")
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
        private ICommand _submitFormCommand;

        /// <summary>
        /// This command is used if the client cancels the form definition.
        /// </summary>
        public ICommand resetFormCommand
        {
            get
            {
                if (_resetFormCommand == null)
                {
                    _resetFormCommand = new ActionCommand(execute =>
                        {
                            formDefModel.Clear();
                            visibleDefinition = "Collapsed";
                            visibleView = "Collapsed";
                            OnChanged("formDefModel");
                        }, canExecute => formDefModel.Count != 0);
                }
                return _resetFormCommand;
            }
        }
        private ICommand _resetFormCommand;

        #endregion
    }
}
