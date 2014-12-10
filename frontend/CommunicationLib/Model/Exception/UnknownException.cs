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
        private int _number = 9999;
        new public int number { get { return _number; }}
        
        public UnknownException()
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
        /// <param name="message"></param>
        /// <param name="inner"></param>
        public UnknownException(string message, System.Exception inner)
         : base(message, inner)
        { 
        }


    }
}
