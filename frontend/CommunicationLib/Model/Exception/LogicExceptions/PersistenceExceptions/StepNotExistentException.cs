using CommunicationLib.Exception;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    class StepNotExistentException : NotExistentException
    {
         /// <summary>
        /// This Exception es a child of DoesntExistsException. 
        /// Each Exception has an number, here it is 11254. 
        /// </summary>
        private int _number = 11254;
        new public int number { get { return _number; } }

        public StepNotExistentException()
            : base("Der Step existiert nicht.")
        {

        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public StepNotExistentException(string message)
            : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message"></param>
        /// <param name="inner"></param>
        public StepNotExistentException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
