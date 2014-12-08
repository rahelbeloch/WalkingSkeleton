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
    }
}
