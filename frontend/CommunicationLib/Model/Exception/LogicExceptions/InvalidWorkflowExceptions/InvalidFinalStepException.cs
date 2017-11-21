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
    public class InvalidFinalStepException : InvalidWorkflowException
    {
        /// <summary>
        /// Each Exception has an number, here it is 12100. 
        /// </summary>
        new public int number { get { return _number; } }
        private int _number = 11530;

        /// <summary>
        /// Default constructor.
        /// </summary>
        public InvalidFinalStepException()
            : base("Ein Endzustand darf keine Nachfolger haben.")
        {
        }

        /// <summary>
        /// Default constructor.
        /// </summary>
        public InvalidFinalStepException(string message)
         : base(message)
        {
        }

        /// <summary>
        /// Default constructor.
        /// </summary>
        public InvalidFinalStepException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
