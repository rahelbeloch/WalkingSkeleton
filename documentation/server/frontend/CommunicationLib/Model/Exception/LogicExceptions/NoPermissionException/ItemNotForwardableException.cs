using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    /// <summary>
    /// This Exception es a child of NoPermissionException.
    /// </summary>
    class ItemNotForwardableException : NoPermissionException
    {
        /// <summary>
        /// Each Exception has an number, here it is 12100. 
        /// </summary>
        new public int number { get { return _number; } }
        private int _number = 11320;

        /// <summary>
        /// Default constructor.
        /// </summary>
        public ItemNotForwardableException()
            : base("Das Item ist nicht weiterschaltbar.")
        {
        }

        /// <summary>
        /// Default constructor.
        /// </summary>
        public ItemNotForwardableException(string message)
            : base(message)
        {
        }

        /// <summary>
        /// Default constructor.
        /// </summary>
        public ItemNotForwardableException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
