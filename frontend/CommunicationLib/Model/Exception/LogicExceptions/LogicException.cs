using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class LogicException : BasicException
    {
        /// <summary>
        /// This Exception es a child of BasicException. 
        /// Each Exception has an number, here it is 11000. 
        /// </summary>
        private int _number = 11000;
        new public int number { get { return _number; } }

         public LogicException()
             :base("Es ist ein Fehler in der Logik aufgetreten.")
        {

        }

         /// <summary>
         /// This constructor allows to add a spezial message.
         /// </summary>
         /// <param name="message">the message</param>
        public LogicException(string message)
         : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message"></param>
        /// <param name="inner"></param>
        public LogicException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
