using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class WorkflowMustTerminateException : InvalidWorkflowException
    {
        private int _number = 11520;
        new public int number { get { return _number; } }

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
