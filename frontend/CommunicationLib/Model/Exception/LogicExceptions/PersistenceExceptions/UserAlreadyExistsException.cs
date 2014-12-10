using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    class UserAlreadyExistsException : AlreadyExistsException
    {
        /// <summary>
        /// This Exception es a child of AlreadyExistsException. 
        /// Each Exception has an number, here it is 11221. 
        /// </summary>
        private int _number = 11221;
        new public int number { get { return _number; } }

        public UserAlreadyExistsException()
            : base("Der Nutzer existiert bereits.")
        {

        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public UserAlreadyExistsException(string message)
            : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message"></param>
        /// <param name="inner"></param>
        public UserAlreadyExistsException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
