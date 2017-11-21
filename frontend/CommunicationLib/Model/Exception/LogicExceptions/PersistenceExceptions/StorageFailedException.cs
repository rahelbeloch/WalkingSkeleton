using CommunicationLib.Exception;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    /// <summary>
    /// This Exception es a child of DoesntExistsException. 
    /// </summary>
    public class StorageFailedException : PersistenceException
    {
        /// <summary>
        /// Each Exception has an number, here it is 11210. 
        /// </summary>
        new public int number { get { return _number; } }
        private int _number = 11210;

        /// <summary>
        /// Default constructor.
        /// </summary>
        public StorageFailedException()
            : base("Das Persistieren eines Elements schlug fehl.")
        {

        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public StorageFailedException(string message)
            : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message"></param>
        /// <param name="inner"></param>
        public StorageFailedException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
