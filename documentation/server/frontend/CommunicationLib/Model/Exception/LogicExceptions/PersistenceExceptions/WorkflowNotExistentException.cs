using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    /// <summary>
    /// Exception for a not existent workflow. It is a child of NotExistentException.
    /// </summary>
    public class WorkflowNotExistentException : NotExistentException
    {
        /// <summary>
        /// Each Exception has an number, here it is 11252. 
        /// </summary>
        new public int number { get { return _number; } }
        private int _number = 11252;
        
        /// <summary>
        /// Default constructor, calling its super constructor.
        /// </summary>
        public WorkflowNotExistentException()
            : base("Der Workflow existiert nicht.")
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public WorkflowNotExistentException(string message)
            : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message">the message</param>
        /// <param name="inner">the inner exception</param>
        public WorkflowNotExistentException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}