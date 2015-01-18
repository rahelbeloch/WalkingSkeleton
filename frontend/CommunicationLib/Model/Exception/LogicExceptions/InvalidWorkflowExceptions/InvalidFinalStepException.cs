using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class InvalidFinalStepException : InvalidWorkflowException
    {
        private int _number = 11530;
        new public int number { get { return _number; } }

        public InvalidFinalStepException()
            : base("Ein Endzustand darf keine Nachfolger haben.")
        {

        }

        public InvalidFinalStepException(string message)
         : base(message)
        {
        }

        public InvalidFinalStepException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
