using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AdminClient.model
{
    /// <summary>
    /// Backing bean implementation for Action.
    /// </summary>
    class Action : AbstractAction
    {
        private string _label = "Aktion";
        public string label { get { return _label; } set { } }
    }
}
