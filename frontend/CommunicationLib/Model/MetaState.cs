using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Model
{
    /// <summary>
    /// This Enum represents the state of a Step in the MetaEntryList
    /// </summary>
    public enum MetaState
    {
        INACTIVE,
        OPEN,
        BUSY,
        DONE,
    }
}
