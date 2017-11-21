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
    class UserHasNoPermissionException : NoPermissionException
    {
        /// <summary>
        /// Each Exception has an number, here it is 12100. 
        /// </summary>
        new public int number { get { return _number; } }
        private int _number = 11310;

        /// <summary>
        /// Default constructor.
        /// </summary>
        public UserHasNoPermissionException()
            : base("Der Nutzer hat keine Erlaubnis.")
        {
        }

        /// <summary>
        /// Default constructor.
        /// </summary>
        public UserHasNoPermissionException(string message)
            : base(message)
        {
        }

        /// <summary>
        /// Default constructor.
        /// </summary>
        public UserHasNoPermissionException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
