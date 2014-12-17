using Client.View;
using CommunicationLib.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Client.ViewModel
{
    /// <summary>
    /// ViewModel class which holds the data for one Row of an item with the fitting step in the view
    /// </summary>
    public class DashboardRow
    {
        private Item _actItem;
        public Item actItem { get { return _actItem; } set { _actItem = value; } }
        private Step _actStep;
        public Step actStep { get { return _actStep; } set { _actStep = value; } }
        private String _username;
        public String username { get { return _username; } }
        public DashboardRow(Item actItem, Step actStep, String username)
        {
            _username = username;
            _actItem = actItem;
            _actStep = actStep;
        }
    }
}
