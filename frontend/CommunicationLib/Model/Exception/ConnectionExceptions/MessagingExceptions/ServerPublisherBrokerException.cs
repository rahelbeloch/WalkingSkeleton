using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class ServerPublisherBrokerException : MessagingException
    {
        /// <summary>
        /// This Exception es a child of ConnectionException. 
        /// Each Exception has an number, here it is 12100. 
        /// </summary>
        private int _number = 12210;
        new public int number { get { return _number; } }

        public ServerPublisherBrokerException()
            :base("Es ist ein Fehler beim Server Publisher Broker passiert.")
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public ServerPublisherBrokerException(string message)
         : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message"></param>
        /// <param name="inner"></param>
        public ServerPublisherBrokerException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
