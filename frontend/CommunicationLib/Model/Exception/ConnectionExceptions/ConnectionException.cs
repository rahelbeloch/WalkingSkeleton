using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    /// <summary>
    /// This Exception es a child of BasicException. 
    /// </summary>
    public class ConnectionException : BasicException
    {
        /// <summary>
        /// Each Exception has an number, here it is 12000. 
        /// </summary>
        new public int number { get { return _number; } }
        private int _number = 12000;

        /// <summary>
        /// Default constructor.
        /// </summary>
        public ConnectionException()
            : base("Es ist ein Fehler bei der Übertragung passiert.")
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public ConnectionException(string message)
         : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message"></param>
        /// <param name="inner"></param>
        public ConnectionException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}