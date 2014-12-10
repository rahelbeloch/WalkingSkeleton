using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class EleAlreadyExistsException : PersistenceException
    {
        /// <summary>
        /// This Exception es a child of PersistenceException. 
        /// Each Exception has an number, here it is 11220. 
        /// </summary>
        private int _number = 11220;
        new public int number { get { return _number; } }

        public EleAlreadyExistsException()
        {

        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public EleAlreadyExistsException(string message)
         : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message"></param>
        /// <param name="inner"></param>
        public EleAlreadyExistsException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
