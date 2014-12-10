using Client.View;
using CommunicationLib.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Client.ViewModel
{
    public class DashboardRow
    {
        private Item _actItem;
        public Item actItem { get { return _actItem; } }
        private Step _actStep;
        public Step actStep { get { return _actStep; } }
        private MyToggleButton _toggleButton;
        public MyToggleButton toggleButton { get { return _toggleButton; } }
        public DashboardRow(Item actItem, Step actStep, String username)
        {
            _actItem = actItem;
            _actStep = actStep;
            _toggleButton = new MyToggleButton(actItem.id, actStep.id, username, _actItem.getState());
        }
    }
}
