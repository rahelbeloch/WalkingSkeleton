using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    /// <summary>
    /// This Exception es a child of LogicException. For all problems with persistence on the server.
    /// </summary>
    public class PersistenceException : LogicException
    {
        /// <summary>
        /// Each Exception has an number, here it is 11200. 
        /// </summary>
        new public int number { get { return _number; } }
        private int _number = 11200;
        
        /// <summary>
        /// Default constructor.
        /// </summary>
        public PersistenceException()
            : base("Fehler in der Datenbank.")
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public PersistenceException(string message)
         : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message">the message</param>
        /// <param name="inner">the inner exception</param>
        public PersistenceException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}