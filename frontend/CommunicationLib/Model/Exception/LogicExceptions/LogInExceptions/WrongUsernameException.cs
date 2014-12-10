using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    class WrongUsernameException : LogInException
    {
        /// <summary>
        /// This Exception es a child of LogInException. 
        /// Each Exception has an number, here it is 11120. 
        /// </summary>
        private int _number = 11120;
        new public int number { get { return _number; } }

        public WrongUsernameException()
            : base("Der Nutzername war falsch.")
        {

        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public WrongUsernameException(string message)
            : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message"></param>
        /// <param name="inner"></param>
        public WrongUsernameException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
