using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class ItemNotExistException : DoesntExistsException
    {
        /// <summary>
        /// This Exception es a child of DoesntExistsException. 
        /// Each Exception has an number, here it is 11253. 
        /// </summary>
        private int _number = 11253;
        new public int number { get { return _number; } }

        public ItemNotExistException()
            :base("Das Item existiert nicht.")
        {

        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public ItemNotExistException(string message)
            : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message"></param>
        /// <param name="inner"></param>
        public ItemNotExistException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
