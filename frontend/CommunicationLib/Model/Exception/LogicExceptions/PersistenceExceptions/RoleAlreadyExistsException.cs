using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    /// <summary>
    /// This Exception es a child of AlreadyExistsException. 
    /// </summary>
    class RoleAlreadyExistsException : AlreadyExistsException
    {
        /// <summary>
        /// Each Exception has an number, here it is 11222. 
        /// </summary>
        new public int number { get { return _number; } }
        private int _number = 11222;

        /// <summary>
        /// Default constructor.
        /// </summary>
        public RoleAlreadyExistsException()
            : base("Die Rolle existiert bereits.")
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public RoleAlreadyExistsException(string message)
            : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message"></param>
        /// <param name="inner"></param>
        public RoleAlreadyExistsException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
