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
        new public int number { get { return _number; } }
        private int _number = 11520;
        
        public WorkflowMustTerminateException()
            : base("Es muss immer ein Endzustand erreicht werden können.")
        {

        }

        public WorkflowMustTerminateException(string message)
         : base(message)
        {
        }

        public WorkflowMustTerminateException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
