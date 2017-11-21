using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    /// <summary>
    /// This Exception es a child of LogicException. 
    /// </summary>
    public class LogInException : LogicException
    {
        /// <summary>
        /// Each Exception has an number, here it is 11100. 
        /// </summary>
        new public int number { get { return _number; } }
        private int _number = 11100;

        /// <summary>
        /// Default constructor.
        /// </summary>
        public LogInException()
            :base("Der LogIn war nicht erfolgreich.")
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public LogInException(string message)
         : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message"></param>
        /// <param name="inner"></param>
        public LogInException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
