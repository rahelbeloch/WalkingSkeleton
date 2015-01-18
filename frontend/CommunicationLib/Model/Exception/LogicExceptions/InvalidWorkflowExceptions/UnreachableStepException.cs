using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class UnreachableStepException : InvalidWorkflowException
    {
        private int _number = 11540;
        new public int number { get { return _number; } }

        public UnreachableStepException()
            : base("Mindestens ein Element ist nicht erreichbar.")
        {

        }

        public UnreachableStepException(string message)
         : base(message)
        {
        }

        public UnreachableStepException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
