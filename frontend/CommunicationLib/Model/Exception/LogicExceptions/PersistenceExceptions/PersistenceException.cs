using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class PersistenceException : LogicException
    {
        /// <summary>
        /// This Exception es a child of LogicException. 
        /// Each Exception has an number, here it is 11200. 
        /// </summary>
        private int _number = 11200;
        new public int number { get { return _number; } }

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
        /// <param name="message"></param>
        /// <param name="inner"></param>
        public PersistenceException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
