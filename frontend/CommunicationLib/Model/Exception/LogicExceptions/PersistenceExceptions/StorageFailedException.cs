using CommunicationLib.Exception;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    class StorageFailedExceptionException : PersistenceException
    {
         /// <summary>
        /// This Exception es a child of DoesntExistsException. 
        /// Each Exception has an number, here it is 11210. 
        /// </summary>
        private int _number = 11210;
        new public int number { get { return _number; } }

        public StorageFailedExceptionException()
            : base("Das Persistieren eines Elements schlug fehl.")
        {

        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public StorageFailedExceptionException(string message)
            : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message"></param>
        /// <param name="inner"></param>
        public StorageFailedExceptionException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
