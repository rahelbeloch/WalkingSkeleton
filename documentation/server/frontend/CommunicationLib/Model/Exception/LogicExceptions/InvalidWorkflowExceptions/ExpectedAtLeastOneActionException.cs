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
    public class ExpectedAtLeastOneActionException : InvalidWorkflowException
    {
        /// <summary>
        /// Each Exception has an number, here it is 12100. 
        /// </summary>
        new public int number { get { return _number; } }
        private int _number = 11570;

        /// <summary>
        /// Default constructor.
        /// </summary>
        public ExpectedAtLeastOneActionException()
            : base("Es muss mindestens eine Aktion vorhanden sein.")
        {
        }

        /// <summary>
        /// Default constructor.
        /// </summary>
        public ExpectedAtLeastOneActionException(string message)
         : base(message)
        {
        }

        /// <summary>
        /// Default constructor.
        /// </summary>
        ExpectedAtLeastOneActionException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
