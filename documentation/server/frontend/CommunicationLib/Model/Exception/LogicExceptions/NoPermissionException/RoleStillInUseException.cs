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
    class RoleStillInUseException : NoPermissionException
    {
        /// <summary>
        /// Each Exception has an number, here it is 12100. 
        /// </summary>
        new public int number { get { return _number; } }
        private int _number = 11350;

        /// <summary>
        /// Default constructor.
        /// </summary>
        public RoleStillInUseException()
            : base("Die Rolle kann nicht gelöscht werden, da sie noch benutzt wird.")
        {
        }

        /// <summary>
        /// Default constructor.
        /// </summary>
        public RoleStillInUseException(string message)
            : base(message)
        {
        }

        /// <summary>
        /// Default constructor.
        /// </summary>
        public RoleStillInUseException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
