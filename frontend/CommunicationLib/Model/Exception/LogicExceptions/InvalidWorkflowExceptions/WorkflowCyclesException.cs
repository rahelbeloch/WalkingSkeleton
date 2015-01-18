using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class WorkflowCyclesException : InvalidWorkflowException
    {
        private int _number = 11510;
        new public int number { get { return _number; } }

        public WorkflowCyclesException()
            : base("Ein Zyklus in einem Workflow ist nicht erlaubt.")
        {

        }

        public WorkflowCyclesException(string message)
         : base(message)
        {
        }

        public WorkflowCyclesException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
