using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;


namespace CommunicationLib.Exception
{
    /// <summary>
    /// UnknownException. 
    /// Each Exception has an number, here it is 9999. 
    /// </summary>
    public class UnknownException : BasicException
    {
        /// <summary>
        /// Each Exception has an number.
        /// </summary>
        new public int number { get { return _number; }}
        private int _number = 9999;
        
        /// <summary>
        /// Default constructor,
        /// </summary>
        public UnknownException()
            : base("Es ist ein unbekannter Fehler aufgetreten.")
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message
        /// </summary>
        /// <param name="message">the message</param>
        public UnknownException(string message)
         : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception
        /// </summary>
        /// <param name="message">the message</param>
        /// <param name="inner">the inner exception</param>
        public UnknownException(string message, System.Exception inner)
         : base(message, inner)
        { 
        }
    }
}