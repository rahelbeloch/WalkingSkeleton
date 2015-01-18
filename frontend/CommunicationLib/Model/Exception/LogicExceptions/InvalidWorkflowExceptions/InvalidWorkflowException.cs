using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class InvalidWorkflowException : LogicException
    {
        private int _number = 11500;
        new public int number { get { return _number; } }

        public InvalidWorkflowException()
            : base("Der Workflow ist invalide.")
        {

        }

        public InvalidWorkflowException(string message)
         : base(message)
        {
        }

        public InvalidWorkflowException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
