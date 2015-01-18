using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class ExpectedOneStartStepException : InvalidWorkflowException
    {
        private int _number = 11550;
        new public int number { get { return _number; } }

        public ExpectedOneStartStepException()
            : base("Es muss genau ein Startzustand existieren.")
        {

        }

        public ExpectedOneStartStepException(string message)
         : base(message)
        {
        }

        public ExpectedOneStartStepException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
