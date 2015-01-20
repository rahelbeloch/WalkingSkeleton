using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class InvalidAddressException : ConnectionException
    {
        /// <summary>
        /// This Exception es a child of BasicException. 
        /// Each Exception has an number, here it is 12000. 
        /// </summary>
        private int _number = 12400;
        new public int number { get { return _number; } }

        public InvalidAddressException()
            : base("Die Server- oder Broker Adresse ist syntaktisch nicht korrekt.")
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public InvalidAddressException(string message)
         : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message"></param>
        /// <param name="inner"></param>
        public InvalidAddressException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
