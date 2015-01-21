using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    /// <summary>
    /// This Exception es a child of InvalidWorkflowException.
    /// </summary>
    public class InvalidWorkflowException : LogicException
    {
        /// <summary>
        /// Each Exception has an number, here it is 12100. 
        /// </summary>
        new public int number { get { return _number; } }
        private int _number = 11500;

        /// <summary>
        /// Default constructor.
        /// </summary>
        public InvalidWorkflowException()
            : base("Der Workflow ist invalide.")
        {
        }

        /// <summary>
        /// Default constructor.
        /// </summary>
        public InvalidWorkflowException(string message)
         : base(message)
        {
        }

        /// <summary>
        /// Default constructor.
        /// </summary>
        public InvalidWorkflowException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
