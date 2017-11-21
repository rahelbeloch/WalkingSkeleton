using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{

    /// <summary>
    /// Exception for invalid workflows. Especially if a workflow is cyclic.
    /// </summary>
    public class WorkflowCyclesException : InvalidWorkflowException
    {
        /// <summary>
        /// Each Exception has an number.
        /// </summary>
        new public int number { get { return _number; } }
        private int _number = 11510;
        
        /// <summary>
        /// Default constructor.
        /// </summary>
        public WorkflowCyclesException()
            : base("Ein Zyklus in einem Workflow ist nicht erlaubt.")
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message.
        /// </summary>
        /// <param name="message">the message</param>
        public WorkflowCyclesException(string message)
         : base(message)
        {
        }

        /// <summary>
        /// This constructor allows to add a spezial message and an other exception.
        /// </summary>
        /// <param name="message">the message</param>
        /// <param name="inner">the inner exception</param>
        public WorkflowCyclesException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}