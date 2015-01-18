using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class ExpectedAtLeastOneActionException : InvalidWorkflowException
    {
        private int _number = 11570;
        new public int number { get { return _number; } }

        public ExpectedAtLeastOneActionException()
            : base("Es muss mindestens eine Aktion vorhanden sein.")
        {

        }

        public ExpectedAtLeastOneActionException(string message)
         : base(message)
        {
        }
        ExpectedAtLeastOneActionException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
