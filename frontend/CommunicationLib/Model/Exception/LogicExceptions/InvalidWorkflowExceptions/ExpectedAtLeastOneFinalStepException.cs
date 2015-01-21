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
    public class ExpectedAtLeastOneFinalStepException : InvalidWorkflowException
    {
        /// <summary>
        /// Each Exception has an number, here it is 12100. 
        /// </summary>
        new public int number { get { return _number; } }
        private int _number = 11560;

        /// <summary>
        /// Default constructor.
        /// </summary>
        public ExpectedAtLeastOneFinalStepException()
            : base("Es muss mindestens ein Endzustand vorhanden sein.")
        {

        }

        /// <summary>
        /// Default constructor.
        /// </summary>
        public ExpectedAtLeastOneFinalStepException(string message)
         : base(message)
        {
        }

        /// <summary>
        /// Default constructor.
        /// </summary>
        public ExpectedAtLeastOneFinalStepException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
