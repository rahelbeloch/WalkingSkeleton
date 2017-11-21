using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    /// <summary>
    /// This Exception es a child of ConnectionException. 
    /// </summary>
    public class JacksonException : RestException
    {
        /// <summary>
        /// Each Exception has an number, here it is 12200. 
        /// </summary>
        new public int number { get { return _number; } }
        private int _number = 12110;

        /// <summary>
        /// Default constructor.
        /// </summary>
        public JacksonException()
            : base("Es ist ein Fehler beim Serialisieren passiert.")
        {

        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public JacksonException(string message)
         : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message"></param>
        /// <param name="inner"></param>
        public JacksonException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}