using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    /// <summary>
    /// This Exception es a child of PersistenceException. 
    /// </summary>
    public class ElementChangedException : PersistenceException
    {
        /// <summary>
        /// Each Exception has an number, here it is 11240. 
        /// </summary>
        new public int number { get { return _number; } }
        private int _number = 11240;

        /// <summary>
        /// Default constructor.
        /// </summary>
        public ElementChangedException()
            : base("Das Element hat sich verändert.")
        {
        }
        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public ElementChangedException(string message)
         : base(message)
        {
        }
        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message"></param>
        /// <param name="inner"></param>
        public ElementChangedException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
