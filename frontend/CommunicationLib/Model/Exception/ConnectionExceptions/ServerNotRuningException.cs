using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class ServerNotRunningException : RestException
    {
        /// <summary>
        /// This Exception es a child of RestException. 
        /// Each Exception has an number, here it is 12210. 
        /// </summary>
        private int _number = 12300;
        new public int number { get { return _number; } }

        public ServerNotRunningException()
            : base("Die Verbindung zum Server ist fehlgeschlagen")
        {

        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public ServerNotRunningException(string message)
         : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message"></param>
        /// <param name="inner"></param>
        public ServerNotRunningException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
