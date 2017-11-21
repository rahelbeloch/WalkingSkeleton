using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    /// <summary>
    /// This Exception es a child of RestException. For Errors if the server is not running/unreachable.
    /// </summary>
    public class ServerNotRunningException : RestException
    {
        /// <summary>
        /// Each Exception has an number, here it is 12210. 
        /// </summary>
        new public int number { get { return _number; } }
        private int _number = 12300;
        
        /// <summary>
        /// Default constructor.
        /// </summary>
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
        /// <param name="message">the message</param>
        /// <param name="inner">the other exception</param>
        public ServerNotRunningException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}