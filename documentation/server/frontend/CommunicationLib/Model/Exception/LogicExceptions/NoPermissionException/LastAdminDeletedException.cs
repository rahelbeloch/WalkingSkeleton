using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    /// <summary>
    /// This Exception es a child of InvalidWorkflowException.
    /// </summary>
    class LastAdminDeletedException : NoPermissionException
    {
        /// <summary>
        /// Each Exception has an number, here it is 12100. 
        /// </summary>
        new public int number { get { return _number; } }
        private int _number = 11340;

        /// <summary>
        /// Default constructor.
        /// </summary>
        public LastAdminDeletedException()
            : base("Der letzte Admin darf nicht gelöscht werden.")
        {
        }

        /// <summary>
        /// Default constructor.
        /// </summary>
        public LastAdminDeletedException(string message)
            : base(message)
        {
        }

        /// <summary>
        /// Default constructor.
        /// </summary>
        public LastAdminDeletedException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
