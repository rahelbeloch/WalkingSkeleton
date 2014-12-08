using Client.View;
using CommunicationLib.Model;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace Client.ViewModel
{
    class DashboardRow
    {
        private Item _actItem;
        private Step _actStep;
        private MyToggleButton toggleButton;
        public DashboardRow(Item actItem, Step actStep, String username)
        {
            _actItem = actItem;
            _actStep = actStep;
            toggleButton = new MyToggleButton(actItem.id, actStep.id, username);
        }
    }
}
