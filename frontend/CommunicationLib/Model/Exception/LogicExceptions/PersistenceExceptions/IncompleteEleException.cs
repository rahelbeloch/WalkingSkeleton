using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class IncompleteEleException : PersistenceException
    {
        private int _number = 11210;
        new public int number { get { return _number; } }

        public IncompleteEleException()
            : base("Das Element ist nicht vollständig.")
        {

        }

        public IncompleteEleException(string message)
         : base(message)
        {
        }

        public IncompleteEleException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
