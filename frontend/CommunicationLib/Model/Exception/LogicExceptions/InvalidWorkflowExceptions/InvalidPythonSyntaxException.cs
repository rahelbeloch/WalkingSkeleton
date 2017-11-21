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
    public class InvalidPythonSyntaxException : InvalidWorkflowException
    {
        /// <summary>
        /// Each Exception has an number, here it is 12100. 
        /// </summary>
        new public int number { get { return _number; } }
        private int _number = 11580;

        /// <summary>
        /// Default constructor.
        /// </summary>
        public InvalidPythonSyntaxException()
            : base("Der Python Syntax ist inkorrekt.")
        {
        }

        /// <summary>
        /// Default constructor.
        /// </summary>
        public InvalidPythonSyntaxException(string message)
         : base(message)
        {
        }

        /// <summary>
        /// Default constructor.
        /// </summary>
        public InvalidPythonSyntaxException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
