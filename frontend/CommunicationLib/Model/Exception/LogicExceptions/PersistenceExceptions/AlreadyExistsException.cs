using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CommunicationLib.Exception
{
    public class AlreadyExistsException : PersistenceException
    {
        private int _number = 11220;
        new public int number { get { return _number; } }

        public AlreadyExistsException()
            : base("Das Element existiert bereits.")
        {
        }

        public AlreadyExistsException(string message)
            : base(message)
        {
        }

        public AlreadyExistsException(string message, System.Exception inner)
            : base(message, inner)
        {
        }
    }
}
