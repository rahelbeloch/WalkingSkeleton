using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    /// <summary>
    /// Exception for invalid workflows. E.g. if a workflow doesn't end in a final step.
    /// </summary>
    public class WorkflowMustTerminateException : InvalidWorkflowException
    {
        /// <summary>
        /// Each Exception has an number.
        /// </summary>
        new public int Number { get { return _number; } }
        private int _number = 11520;
        
        /// <summary>
        /// Default constructor.
        /// </summary>
        public WorkflowMustTerminateException()
            : base("Es muss immer ein Endzustand erreicht werden können.")
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public WorkflowMustTerminateException(string message)
         : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message">the message</param>
        /// <param name="inner">the inner exception</param>
        public WorkflowMustTerminateException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}