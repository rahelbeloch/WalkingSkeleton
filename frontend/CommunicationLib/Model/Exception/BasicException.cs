using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;


namespace CommunicationLib.Exception
{   
    /// <summary>
    /// BasicException, all other Exceptions inherit from this Exception. 
    /// Each Exception has an number, here it is 10000 
    /// </summary>
    public class BasicException : System.Exception
    {
        /// <summary>
        /// Each Exception has an number, here it is 12100. 
        /// </summary>
        public int number { get { return _number; }}
        private int _number = 10000;

        /// <summary>
        /// Each Exception has an number, here it is 12100. 
        /// </summary>
        public BasicException()
            : base("Es ist ein Fehler aufgetreten.")
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message
        /// </summary>
        /// <param name="message">the message</param>
        public BasicException(string message)
         : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception
        /// </summary>
        /// <param name="message"></param>
        /// <param name="inner"></param>
        public BasicException(string message, System.Exception inner)
         : base(message, inner)
        { 
        }


    }
}
