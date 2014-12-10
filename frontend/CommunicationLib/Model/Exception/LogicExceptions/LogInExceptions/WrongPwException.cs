using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class WrongPwException : LogInException
    {
        /// <summary>
        /// This Exception es a child of LogIn. 
        /// Each Exception has an number, here it is 11110. 
        /// </summary>
        private int _number = 11110;
        new public int number { get { return _number; } }

        public WrongPwException()
            : base("Das Passwort war falsch.")
        {

        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public WrongPwException(string message)
         : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message"></param>
        /// <param name="inner"></param>
        public WrongPwException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
