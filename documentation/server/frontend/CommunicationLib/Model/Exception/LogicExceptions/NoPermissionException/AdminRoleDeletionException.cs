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
    class AdminRoleDeletionException : NoPermissionException
    {
        /// <summary>
        /// Each Exception has an number, here it is 12100. 
        /// </summary>
        new public int number { get { return _number; } }
        private int _number = 11330;

        /// <summary>
        /// Default constructor.
        /// </summary>
        public AdminRoleDeletionException()
            : base("Die Rolle Admin darf nicht gelöscht werden.")
        {
        }

        /// <summary>
        /// Default constructor.
        /// </summary>
        public AdminRoleDeletionException(string message)
            : base(message)
        {
        }

        /// <summary>
        /// Default constructor.
        /// </summary>
        public AdminRoleDeletionException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
