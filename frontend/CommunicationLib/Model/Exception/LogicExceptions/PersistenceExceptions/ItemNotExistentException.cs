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
    public class ItemNotExistentException : NotExistentException
    {
        /// <summary>
        /// Each Exception has an number, here it is 11253. 
        /// </summary>
        new public int number { get { return _number; } }
        private int _number = 11253;

        /// <summary>
        /// Default constructor.
        /// </summary>
        public ItemNotExistentException()
            :base("Das Item existiert nicht.")
        {

        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public ItemNotExistentException(string message)
            : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message"></param>
        /// <param name="inner"></param>
        public ItemNotExistentException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
