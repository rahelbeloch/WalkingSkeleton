using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    class RoleNotExistentException : DoesntExistsException
    {
        /// <summary>
        /// This Exception es a child of RoleException. 
        /// Each Exception has an number, here it is 11256. 
        /// </summary>
        private int _number = 11256;
        new public int number { get { return _number; } }

        public RoleNotExistentException()
            : base("Die Rolle existiert nicht.")
        {

        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public RoleNotExistentException(string message)
         : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message"></param>
        /// <param name="inner"></param>
        public RoleNotExistentException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
