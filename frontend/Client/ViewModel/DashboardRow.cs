using Client.View;
using CommunicationLib.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows;

namespace Client.ViewModel
{
    /// <summary>
    /// ViewModel class which holds the data for one Row of an item with the fitting step in the view
    /// </summary>
    public class DashboardRow
    {
        private Item _actItem;
        public Item actItem {
            get { return _actItem; }
            set {
                _actItem = value;
                _actState = _actItem.state;
                if(_actState.Equals("OPEN")) {
                    _visibilityStepForwardButton = Visibility.Visible;
                } else {
                    _visibilityStepForwardButton = Visibility.Hidden;
                }
            } }
        private Step _actStep;
        public Step actStep { get { return _actStep; } set { _actStep = value; } }
        private String _username;
        public String username { get { return _username; } }
        private String _actState;
        private Visibility _visibilityStepForwardButton;
        public Visibility visibilityStepForwardButton { get { return _visibilityStepForwardButton; } }
        public DashboardRow(Item actItem, Step actStep, String username)
        {
            _username = username;
            this.actItem = actItem;
            _actStep = actStep;
        }
    }
}
