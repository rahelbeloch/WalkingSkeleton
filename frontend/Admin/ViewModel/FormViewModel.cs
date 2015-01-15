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
        private FormEntry _selectedDefinition = null;
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
   
        /// <summary>
        /// This property is used as a value setter to hide user controls.
        /// </summary>
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

        /// <summary>
        /// This property will be used for setting a form's id.
        /// </summary>
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

        /// <summary>
        /// This property will be used for setting a form's description.
        /// </summary>
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

        /// <summary>
        /// This method is called when the client receives a form from the server.
        /// </summary>
        /// <param name="form">form which will be added to the form collection</param>
        public void updateForm(Form form)
        {
            _mainViewModel.formCollection.Add(form);
        }
        #endregion

        #region commands

        /// <summary>
        /// This command is executed if the client wants to create a new form.
        /// </summary>
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
                        formDefModel.Add(formEntry);
                        OnChanged("formDefModel");
                        _visible = "Visible";
                        OnChanged("visible");

                    }, canExecute => formDefModel.Count == 0);
                }
                return _addFormCommand;
            }
        }

        /// <summary>
        /// This command is executed if the client adds a new definition to a form.
        /// </summary>
        private ICommand _addDefinitionCommand;
        public ICommand addDefinitionCommand
        {
            get
            {
                if (_addDefinitionCommand == null)
                {
                    _addDefinitionCommand = new ActionCommand(execute =>
                    {
                        var lastEle = formDefModel.Last();

                        if (lastEle.key != "" && lastEle.value != "" && lastEle.value != null)
                        {
                            FormEntry formEntry = new FormEntry();
                            formEntry.key = "";
                            formDefModel.Add(formEntry);
                            OnChanged("formDefModel");
                        }

                    }, canExecute => formDefModel.Count != 0);
                }
                return _addDefinitionCommand;
            }
        }

        /// <summary>
        /// This method is used if the client removes a selected definition.
        /// </summary>
        private ICommand _removeDefinitionCommand;
        public ICommand removeDefinitionCommand
        {
            get
            {
                if (_removeDefinitionCommand == null)
                {
                    _removeDefinitionCommand = new ActionCommand(execute =>
                        {
                            foreach (FormEntry fe in formDefModel)
                            {
                                if (fe.selected)
                                {
                                    _selectedDefinition = fe;
                                    break;
                                }
                            }
                            formDefModel.Remove(_selectedDefinition);
                            if (formDefModel.Count == 0)
                            {
                                formDefModel.Clear();
                                _visible = "Hidden";
                                OnChanged("visible");
                            }
                            OnChanged("formDefModel");
                        }, canExecute =>
                            {
                                foreach (FormEntry fe in formDefModel)
                                {
                                    if (fe.selected)
                                    {
                                        return true;
                                    }
                                }
                                return false;
                            });
                }
                return _removeDefinitionCommand;
            }
        }

        /// <summary>
        /// This command is used if the client wants to submit a form to the server.
        /// </summary>
        private ICommand _submitFormCommand;
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

        /// <summary>
        /// This command is used if the client cancels the form definition.
        /// </summary>
        private ICommand _resetFormCommand;
        public ICommand resetFormCommand
        {
            get
            {
                if (_resetFormCommand == null)
                {
                    _resetFormCommand = new ActionCommand(execute =>
                        {
                            formDefModel.Clear();
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
