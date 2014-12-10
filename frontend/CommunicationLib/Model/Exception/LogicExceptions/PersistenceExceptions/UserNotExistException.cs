using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{

    public class UserNotExistException : DoesntExistsException
    {
        /// <summary>
        /// This Exception es a child of DoesntExistsException. 
        /// Each Exception has an number, here it is 11251.
        /// </summary>
        private int _number = 11251;
        new public int number { get { return _number; } }

        public UserNotExistException()
            : base("Der Nutzer existiert nicht.")
        {

        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public UserNotExistException(string message)
         : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message"></param>
        /// <param name="inner"></param>
        public UserNotExistException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
