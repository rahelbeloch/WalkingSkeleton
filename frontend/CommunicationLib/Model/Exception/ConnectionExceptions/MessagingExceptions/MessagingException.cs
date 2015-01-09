using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class MessagingException : ConnectionException
    {
        /// <summary>
        /// This Exception es a child of ConnectionException. 
        /// Each Exception has an number, here it is 12100. 
        /// </summary>
        private int _number = 12200;
        new public int number { get { return _number; } }

        public MessagingException()
            :base("Es ist ein fehler beim Aktualisieren aufgetreten.")
        {

        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public MessagingException(string message)
         : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message"></param>
        /// <param name="inner"></param>
        public MessagingException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
