using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    class UserHasAlreadyRoleException : AlreadyExistsException
    {
        /// <summary>
        /// This Exception es a child of RoleException. 
        /// Each Exception has an number, here it is 11223.
        /// </summary>
        private int _number = 11223;
        new public int number { get { return _number; } }

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
