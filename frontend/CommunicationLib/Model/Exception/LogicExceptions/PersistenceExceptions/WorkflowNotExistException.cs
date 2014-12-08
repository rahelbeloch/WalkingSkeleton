using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class WorkflowNotExistException : DoesntExistsException
    {
        private int _number = 11252;
        new public int number { get { return _number; } }

        public WorkflowNotExistException()
            : base("Der Workflow existiert nicht.")
        {

        }

        public WorkflowNotExistException(string message)
            : base(message)
        {
        }

        public WorkflowNotExistException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
