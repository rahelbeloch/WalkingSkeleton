using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{
    /// <summary>
    /// Backing bean implementation for Action.
    /// </summary>
    public class Action : Step
    {
        public Action(AbstractAction abstractAction) : base(abstractAction)
        {
            label = "Aktion";
        }
    }
}
