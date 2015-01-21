using Client.View;
using CommunicationLib.Model;
using CommunicationLib.Model.DataModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;

namespace Client.ViewModel
{
    /// <summary>
    /// ViewModel class which holds the data for one row of an item with the fitting step in the view.
    /// </summary>
    public class DashboardRow
    {
        private Item _actItem;
        /// <summary>
        /// the current itemm of the dashboardrow
        /// on setting all depending properties are update
        /// </summary>
        public Item actItem 
        {
            get { return _actItem; }
            set 
            {
                _actItem = value;
                _actState = _actItem.State;
                _visibilityStepForwardButton = _actState.Equals("OPEN") ? Visibility.Visible : Visibility.Collapsed;
                _visibilityFinishButton = _actState.Equals("BUSY") ? Visibility.Visible : Visibility.Hidden;

                _formRows = new List<FormRow>();
                foreach (FormEntry formEntry in _form.FormDef)
                {
                    _formRows.Add(new FormRow(_actItem, formEntry.key, formEntry.Datatype));
                }
            }
        }

        private Step _actStep;
        /// <summary>
        /// the current Step
        /// </summary>
        public Step actStep { get { return _actStep; } set { _actStep = value; } }

        private String _username;
        /// <summary>
        /// the username of the logged user
        /// </summary>
        public String username { get { return _username; } }
        /// <summary>
        /// the current state of the item, only used to calculate the Visibility-property
        /// </summary>
        private String _actState;

        private Visibility _visibilityStepForwardButton;
        /// <summary>
        /// Visibility of the "Annehmen"-Button
        /// </summary>
        public Visibility visibilityStepForwardButton { get { return _visibilityStepForwardButton; } }

        private Visibility _visibilityFinishButton;
        /// <summary>
        /// Visibility of the formular and "Abschließen"-Button
        /// </summary>
        public Visibility visibilityFinishButton { get { return _visibilityFinishButton; } }

        private Form _form;
        /// <summary>
        /// The Formular of the current workflow
        /// </summary>
        public Form form { get { return _form; } }

        private List<FormRow> _formRows;
        /// <summary>
        /// List of Formularrows
        /// </summary>
        public List<FormRow> formRows { get { return _formRows; } }
        public DashboardRow(Item actItem, Step actStep, String username, Form form)
        {
            _username = username;
            this._actItem = actItem;
            _actStep = actStep;
            _form = form;

            _actState = _actItem.State;

            _formRows = new List<FormRow>();
            foreach (FormEntry formEntry in _form.FormDef)
            {
                _formRows.Add(new FormRow(_actItem, formEntry.key, formEntry.Datatype));
            }
        }
    }
}