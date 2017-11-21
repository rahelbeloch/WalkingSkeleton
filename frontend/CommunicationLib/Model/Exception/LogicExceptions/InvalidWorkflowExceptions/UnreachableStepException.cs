using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    /// <summary>
    /// Exception for an unreachable step. Child of InvalidWorkflowException.
    /// </summary>
    public class UnreachableStepException : InvalidWorkflowException
    {
        /// <summary>
        /// Each Exception has an number, here it is 11251.
        /// </summary>
        new public int number { get { return _number; } }
        private int _number = 11540;
        
        /// <summary>
        /// Default Constructor.
        /// </summary>
        public UnreachableStepException()
            : base("Mindestens ein Element ist nicht erreichbar.")
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public UnreachableStepException(string message)
         : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message">the message</param>
        /// <param name="inner">the other exception</param>
        public UnreachableStepException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}