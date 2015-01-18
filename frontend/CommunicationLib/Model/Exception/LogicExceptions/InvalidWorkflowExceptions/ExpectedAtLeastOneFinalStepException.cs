using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class ExpectedAtLeastOneFinalStepException : InvalidWorkflowException
    {
        private int _number = 11560;
        new public int number { get { return _number; } }

        public ExpectedAtLeastOneFinalStepException()
            : base("Es muss mindestens ein Endzustand vorhanden sein.")
        {

        }

        public ExpectedAtLeastOneFinalStepException(string message)
         : base(message)
        {
        }

        public ExpectedAtLeastOneFinalStepException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
