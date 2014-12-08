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

        public MyToggleButton()
            : base()
        {

        }
        public MyToggleButton(int stepId, int itemId, String username)
            : base()
        {
            this.stepId = stepId;
            this.itemId = itemId;
            this.username = username;
        }
    }
}
