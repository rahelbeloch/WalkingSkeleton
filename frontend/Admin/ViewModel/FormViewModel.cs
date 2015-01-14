using CommunicationLib;
using CommunicationLib.Exception;
using CommunicationLib.Model;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;

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

        private Form _selectedForm;
        public Form SelectedForm
        {
            get
            {
                return _selectedForm;
            }
            set
            {
                _selectedForm = value;
                OnChanged("selectedForm");
            }
        }
        #endregion

    }
}
