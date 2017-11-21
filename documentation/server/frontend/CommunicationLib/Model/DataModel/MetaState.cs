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
        /// <summary>
        /// State inactive.
        /// </summary>
        INACTIVE,

        /// <summary>
        /// State open.
        /// </summary>
        OPEN,

        /// <summary>
        /// State busy.
        /// </summary>
        BUSY,

        /// <summary>
        /// State done.
        /// </summary>
        DONE,
    }
}