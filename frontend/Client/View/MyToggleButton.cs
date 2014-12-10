using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Controls.Primitives;

namespace Client.View
{
    public class MyToggleButton : ToggleButton
    {
        public int stepId{get; set;}
        public int itemId { get; set; }
        public string username { get; set; }
        private String _state;
        public String state { get { return _state; } set { _state = value; } }
        public MyToggleButton()
            : base()
        {

        }
        public MyToggleButton(int stepId, int itemId, String username, String state)
            : base()
        {
            this.stepId = stepId;
            this.itemId = itemId;
            this.username = username;
            this._state = state;
        }
    }
}
