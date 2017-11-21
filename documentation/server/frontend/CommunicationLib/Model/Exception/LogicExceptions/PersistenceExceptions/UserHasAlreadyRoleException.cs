using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    /// <summary>
    /// This Exception es a child of RoleException. 
    /// </summary>
    class UserHasAlreadyRoleException : AlreadyExistsException
    {
        /// <summary>
        /// Each Exception has an number, here it is 11223.
        /// </summary>
        new public int number { get { return _number; } }
        private int _number = 11223;

        /// <summary>
        /// Default constructor.
        /// </summary>
        public UserHasAlreadyRoleException()
            : base("Der User hat diese Rolle bereits.")
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public UserHasAlreadyRoleException(string message)
            : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        /// <param name="inner">the inner exception</param>
        public UserHasAlreadyRoleException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
