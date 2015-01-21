using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    /// <summary>
    /// Exceptions for errors concerning user operations, if the user doesn't exist. This is a child of NotExistentException.
    /// </summary>
    public class UserNotExistentException : NotExistentException
    {
        /// <summary>
        /// Each Exception has an number, here it is 11251.
        /// </summary>
        new public int number { get { return _number; } }
        private int _number = 11251;
        
        /// <summary>
        /// Default constructor.
        /// </summary>
        public UserNotExistentException()
            : base("Der Nutzer existiert nicht.")
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public UserNotExistentException(string message)
         : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message">the message</param>
        /// <param name="inner">the other exception</param>
        public UserNotExistentException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}